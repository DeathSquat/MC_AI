package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private LinearLayout optionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setTitle("MindCure AI");
        }

        // **Talk to Therapist Button**
        Button btnTalkToTherapist = findViewById(R.id.btnTalkToTherapist);
        btnTalkToTherapist.setOnClickListener(view -> {
            Intent intent = new Intent(ChatActivity.this, BookingActivity.class);
            startActivity(intent);
        });

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        optionsLayout = findViewById(R.id.optionsLayout);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        startChat();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startChat() {
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();

        addBotMessage("Hey Nishchay! 👋 How was your day?");
        showOptions(new String[]{"Had a good moment 😊", "Had a tough time 😞"});
    }

    private void addBotMessage(String message) {
        chatMessages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    private void addUserMessage(String message) {
        chatMessages.add(new ChatMessage(message, false));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    private void showOptions(String[] options) {
        optionsLayout.removeAllViews();
        optionsLayout.setPadding(0, 30, 0, 0);

        for (String option : options) {
            Button button = new Button(this);
            button.setText(option);
            button.setTextSize(16);
            button.setAllCaps(false);
            button.setPadding(16, 8, 16, 8);
            button.setBackgroundResource(R.drawable.option_button_bg);
            button.setGravity(Gravity.CENTER);

            button.setOnClickListener(view -> {
                addUserMessage(option);
                handleUserResponse(option);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 5, 10, 5);
            button.setLayoutParams(params);
            optionsLayout.addView(button);
        }
    }

    private void handleUserResponse(String response) {
        optionsLayout.removeAllViews();

        switch (response) {
            case "Had a good moment 😊":
                addBotMessage("That's wonderful! What made your day special?");
                showOptions(new String[]{"Had fun 🎉", "Achieved something 🏆", "Spent time with loved ones ❤️"});
                break;

            case "Had fun 🎉":
                addBotMessage("That sounds amazing! What kind of fun activity?");
                showOptions(new String[]{"Played a game 🎮", "Went outdoors 🌳", "Tried something new 🔥"});
                break;

            case "Achieved something 🏆":
                addBotMessage("That's impressive! What did you accomplish?");
                showOptions(new String[]{"Completed a task ✅", "Learned something new 📖", "Got appreciated 👏"});
                break;

            case "Spent time with loved ones ❤️":
                addBotMessage("That's heartwarming! Who did you spend time with?");
                showOptions(new String[]{"Family 👨‍👩‍👧‍👦", "Friends 🎊", "Someone special 💖"});
                break;

            case "Had a tough time 😞":
                addBotMessage("I'm sorry to hear that. Want to talk about it?");
                showOptions(new String[]{"Yes, I want to share 💙", "No, not right now."});
                break;

            case "Yes, I want to share 💙":
                addBotMessage("I'm here to listen. What’s on your mind?");
                showOptions(new String[]{"Felt stressed 😰", "Felt lonely 😔", "Had a bad experience 😞"});
                break;

            case "Felt stressed 😰":
            case "Felt lonely 😔":
                addBotMessage("Stress can be tough. Want to try a calming activity?");
                showOptions(new String[]{"Breathing exercise 🌿", "Play a game 🎮", "Just need a chat 💙"});
                break;

            case "Had a bad experience 😞":
                addBotMessage("That must have been hard. Do you want a distraction?");
                showOptions(new String[]{"Play a game 🎮", "Mindfulness activity 🌿", "Just talk 💙"});
                break;

            case "Just talk 💙":
            case "Still need help 💙":
            case "No, not right now.":
            case "Got appreciated 👏":
            case "Learned something new 📖":
            case "Completed a task ✅":
            case "Played a game 🎮":
            case "Went outdoors 🌳":
            case "Tried something new 🔥":
            case "Family 👨‍👩‍👧‍👦":
            case "Friends 🎊":
            case "Someone special 💖":
            case "Just need a chat 💙":
                addBotMessage("Would you like to share more about your day?");
                showOptions(new String[]{"Yes, I want to share more 💙", "No, let's wrap up."});
                break;

            case "Yes, I want to share more 💙":
                addBotMessage("I'm here to listen! Tell me what’s on your mind.");
                showOptions(new String[]{"Had a good moment 😊", "Had a tough time 😞"});
                break;

            case "No, let's wrap up.":
            case "No, I'm good now.":
                addBotMessage("Alright! I'm always here for you. Take care! 💙");
                showOptions(new String[]{"Start a new chat 🔄", "Exit to Home ❌"});
                break;

            case "Start a new chat 🔄":
                restartChat();
                break;

            case "Exit to Home ❌":
                addBotMessage("Alright! Take care and stay strong. See you soon! 😊");
                optionsLayout.postDelayed(() -> {
                    Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }, 2000);
                break;

            case "Breathing exercise 🌿":
                addBotMessage("Great choice! Redirecting you to the breathing exercise... 🌿");
                optionsLayout.postDelayed(() -> {
                    Intent intent = new Intent(ChatActivity.this, BreathingExerciseActivity.class);
                    startActivity(intent);
                }, 2000);
                break;

            case "Play a game 🎮":
                addBotMessage("Great choice! Redirecting you to the game now... 🎮");
                optionsLayout.postDelayed(() -> {
                    Intent intent = new Intent(ChatActivity.this, GamesActivity.class);
                    startActivity(intent);
                    finish();
                }, 2000);
                break;
        }
    }

    private void restartChat() {
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();
        startChat();
    }

    private void scrollToBottom() {
        chatRecyclerView.post(() -> chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1));
    }
}
