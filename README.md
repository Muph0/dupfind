# Dupfind

A command line utility created for my JetBrains Internship application.

This program solves a special case of the common substring problem. It finds duplicate *fragments* between two text documents.

The way the project is set up right now, a *fragment* correspond to an non-empty line of a file.


## Build and use

For building and running outside an IDE use Maven:

    mvn clean compile assembly:single

This should produce an executable jar file.
After compiling you can run the program like so:

    java -jar target/DupFind-1.0-SNAPSHOT-jar-with-dependencies.jar <file1> <file2>

### Output format

When you run the program, it prints the duplicate fragments found between the files like this:

    <offset in file1>:<offset in file2>     : <fragment text>

## How it works

From all the different approaches, I decided on simple indexing by fragment hash.

The program reads each file and constructs an index of the fragments, saving only their hash and offset in the file. Later, the indices are scanned for duplicates, and on hash collision the text is re-read from the file to confirm the duplicate.
