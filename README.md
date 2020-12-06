# MEMAnalyzer - a questionnaire that determines your sense of humor

### Description

Sometimes it makes sense to apply subtle and indirect approaches to testing personal qualities. One of these methods is to study a persons reaction to various types of jokes. Psychoanalysts have long agreed that humor is a kind of window into the "inner recesses" of consciousness, and psychologists in their research have confirmed the existence of a real connection between what kind of humor a person likes and his personality type.

### Recommendations before installation

Before starting, make sure that the server part of the application is running
(link to the backend repository: https://github.com/Seeyouinthespring/MEMAnalyzer_Backend)

Use Android Studio to build and run the project

### Installation

Clone the repository to your computer

```sh
git clone https://github.com/arranay/MEMAnalyzer_FE
```

Open the cloned project in AndroidStudio and open the file java/com/example/memanalyzer/environment/RetrofitObject.kt
Changed BaseUrl to your

```sh
val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzer.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
```

### Launching

You can run the project in a virtual environment (provided by AndroidStudio) or on your phone.

Follow this steps below to run the MEMAnalyzer on your phone:
1. Connect your smartphone to your computer using a USB cable
2. Go to the settings on your device
3. Select the section "About device"
4. Find the item "Build number" and click on it 7 times. A window should appear notifying that developer mode is activated.
5. The developer options section should now appear in the settings. Enable the "USB debugging" option in it.
6. run the project in AndroidStudio
