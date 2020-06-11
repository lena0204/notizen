# Notes application for android

Licensed under Apache 2.0

## Core features
- simple note creation and editing
- categorizing using colors
- password protection of notes
- export / import using a text file
- language support: english, german

## Known issues
- no check (dialog or similar) when deleting a note
- protection is partly not correctly displayed in the notes list but handled correctly when opening a note
- password cannot be changed or reset (can be fixed by exporting the notes and then reinstalling the app)

## Contribution
Feel free to create issues / pull requests or contact me directly for feedback. I look forward to new ideas.

### Extensive feature list
- add new notes
- edit existing notes
- show existing notes
- delete notes, without dialog nor password check currently (!)
- mark them with category
- filter for one or more categories (currently in settings)
- protect notes with a numeric password
- configure name and line number in overview separately for each category
- change design between light and dark
- backup / restore functionality to a plain text file

### Planned features
- change password
- reset password
- safety dialog when deleting

### Technology
Native android app with kotlin, uses jetpack libraries

Made for Android version 5 or greater (API 21)

Made with Android Studio 4.1 Preview

### Screenshots
Notes list
![Notes list](/screenshots/notes_list_light.png)

Edit notes
![Edit note](/screenshots/notes_edit.png)

Settings
![Settings](/screenshots/notes_settings.png)

Dark mode
![Dark mode](/screenshots/notes_list_filtered_dark.png)
