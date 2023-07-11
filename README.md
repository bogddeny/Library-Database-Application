# Library Database Application

This is a small application make for the Object Oriented Programming course at my University.

## Project Requirements

Make a Java Swing application for a library. The purpose of this application is to manage books (add and remove them), search for the books you want, and view the book properties. The books must be stored in a .txt file with the following format:

```
BookType            # There are 2 types literature and science
BookTitle           # The title of the book
BookAuthor          # The name of the book author
BookISBN            # A valid ISBN code
BookReleaseYear     # The release year of the book
BookCategory        # The category to which the book belongs
BookDescription     # (snly science type books) A brief description
```

The application must read/write the file, validate inputs, account for spelling mistakes (in Greek)

## Description of implemented features

- Somewhat resonsive UI to the screen size
- Book reading and writing to the text file
- Adding books via the user interface while checking for valid inputs
- Searching for books (search works regardless of spelling mistakes)
- Removing books
