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
        return "Knock Knock! Who is there?";
    }
}
