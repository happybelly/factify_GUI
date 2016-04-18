# GUI_FactExtractor.jar

1.       Mike’s feedback on UAT: better use temporary folder instead of the user home folder for saving JSON files and Rule_INPUT. 
Done.
-          GUI_FactExtractor will use temporary folder – e.g. on Windows 7 it can be found under C:\Users\%USERNAME%\AppData\Local\Temp\factpub

2.       Integrate the latest FactExtractor module using the Blake Verbs to GUI_FactExtractor.
Done.

3.       “Input parameter error” issue on Windows
Done. (related to the task #2)

4.       “Input parameter error” issue on Mac OS.
Suspended.
-          I’ve come to the conclusion that for debugging this issue, I need to scrutinize the FactExtractor source code, which I currently take as a black box.
-          Based on the conversation with Xiaocheng (please see below), it seems that FactExtractor.jar needs to be properly set up so it can properly accept parameters from outside modules – i.e. Zotero extension / GUI_FactExtractor - on Mac OS.
-          Since it would require me to modify the FactExtractor source code, and I cannot estimate the time to remove the bug, I have suspended this task.

5.       Status keep putting as "Waiting…" for the first few drops, when it runs from runnable.jar; it’s stable when it runs from eclipse.
In progress. 
-          I think this is the most important task as of now. I will make sure to identify the root cause and fix it by the end of this week.

6.       Putting “Drop PDFs Here” image on the main panel.
In progress.
-          I’m going to use the attached image on the UI. Implementation is a bit tricky and takes a time because I need to add some code ScrollPane component, which originally does not support showing image on it.  

You can download the current version of GUI_FactExtractor.jar from the below link:
http://factpub.org/wiki/sun/bootstrap/GUI_FactExtractor_18-Apr-2016_.jar

