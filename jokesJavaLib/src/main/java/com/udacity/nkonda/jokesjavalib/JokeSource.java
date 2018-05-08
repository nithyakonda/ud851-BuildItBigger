package com.udacity.nkonda.jokesjavalib;

public class JokeSource {
    private static JokeSource sInstance;

    private JokeSource() {
    }

    public static JokeSource getInstance() {
        if (sInstance == null) {
            sInstance = new JokeSource();
        }
        return sInstance;
    }

    public String getNewJoke() {
        return "What do you call the soft tissue between a shark's teeth?\n" +
                "\n" +
                "A slow swimmer.";
    }
}
