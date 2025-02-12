//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
//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
package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.*;

public class ChatActivity extends AppCompatActivity {

    private EditText userMessageInput;
    private ImageButton sendButton, backButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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"; // 🔒 Secure API Key storage
    private static final String TAG = "ChatActivity";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userMessageInput = findViewById(R.id.userMessageInput);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        chatAdapter = new ChatAdapter(messageList, this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        userMessageInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                sendMessage();
                return true;
            }
            return false;
        });

        sendButton.setOnClickListener(v -> sendMessage());
        backButton.setOnClickListener(v -> navigateToHome());
    }

    private void sendMessage() {
        try {
            String userMessage = userMessageInput.getText().toString().trim();
            if (userMessage.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }
            addMessage("User", userMessage);
            userMessageInput.setText("");
            hideKeyboard();
            getAIResponse(userMessage);
        } catch (Exception e) {
            Log.e(TAG, "Error sending message", e);
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addMessage(String sender, String message) {
        boolean isUser = sender.equals("User");
        messageList.add(new Message(message, isUser));
        runOnUiThread(() -> {
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    private void getAIResponse(String userMessage) {
        executorService.execute(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                JsonObject requestBodyJson = new JsonObject();
                requestBodyJson.addProperty("model", "gpt-3.5-turbo");

                JsonArray messages = new JsonArray();

                // ✅ Add system message for better response handling
                JsonObject systemMessage = new JsonObject();
                systemMessage.addProperty("role", "system");
                systemMessage.addProperty("content", "You are MindCure AI, a supportive AI designed to help with mental health issues.");
                messages.add(systemMessage);

                for (Message msg : messageList) {
                    JsonObject msgObj = new JsonObject();
                    msgObj.addProperty("role", msg.isUser() ? "user" : "assistant");
                    msgObj.addProperty("content", msg.getText());
                    messages.add(msgObj);
                }

                requestBodyJson.add("messages", messages);

                RequestBody requestBody = RequestBody.create(
                        requestBodyJson.toString(),
                        MediaType.parse("application/json")
                );

                Request request = new Request.Builder()
                        .url(OPENAI_API_URL)
                        .post(requestBody)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                    runOnUiThread(() -> addMessage("MindCure AI", "Oops! Something went wrong. Try again later."));
                    return;
                }

                if (response.body() != null) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "API Response: " + responseBody);

                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonResponse.has("choices")) {
                        JsonArray choices = jsonResponse.getAsJsonArray("choices");
                        if (choices.size() > 0) {
                            JsonObject messageObj = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                            if (messageObj != null && messageObj.has("content")) {
                                String aiResponse = messageObj.get("content").getAsString();
                                runOnUiThread(() -> addMessage("MindCure AI", aiResponse));
                            } else {
                                Log.e(TAG, "Invalid JSON structure: No 'content' found.");
                                runOnUiThread(() -> addMessage("MindCure AI", "Unexpected response format."));
                            }
                        }
                    } else {
                        Log.e(TAG, "Invalid API response: No 'choices' found.");
                        runOnUiThread(() -> addMessage("MindCure AI", "Unexpected API response."));
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "API Request Failed", e);
                runOnUiThread(() -> addMessage("MindCure AI", "Network Error: " + e.getMessage()));
            }
        });
    }

    private void navigateToHome() {
        startActivity(new Intent(ChatActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToHome();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(userMessageInput.getWindowToken(), 0);
    }
}
