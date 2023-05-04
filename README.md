# Socket Based File Transmission System

[![Java CI with Maven](https://github.com/echo-cool/BDIC3027J-Software-Systems-Architecture-Socket-Based-FileTransfer-System/actions/workflows/maven.yml/badge.svg)](https://github.com/echo-cool/BDIC3027J-Software-Systems-Architecture-Socket-Based-FileTransfer-System/actions/workflows/maven.yml)

## Comprehensive Project Report 

The comprehensive report is in `File Transmission System Design.pdf`, that report has covered all the content of this project.


## Build this project
The main class of this project is `Sender` class and `Receiver`, Please run both of these class to run this system.

* Sender class: The one who send files.
* Receiver class: The one who receive files.

## Task Design

One the system is started, you can create many tasks, and add files to the task.

The task will run concurrently, since each task will run on a different thread.

In a task, you can add as many files as you need, files in the task will be sent linear.


## Commands
* `new` Create a new Task.
* `add` Add a file to a Task.
* `q`   Query the current Tasks.
* `start` Start a Task.
* `startall` Start all the Tasks.
* `pause` Pause a Task.
* `resume` Resume a Task.


## Example output

### Input the target ip

```shell
Welcome to the Sender Console
Please enter the IP address of the target machine (Default: localhost):
>>>localhost
OK
Sending files to 127.0.0.1
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}

TaskSend{TaskID=1, numberOfFiles=3}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}
	File{name=test.txt}

>>> 
```

### Creating a Task


```shell
>>> new
Task 2 created
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}

TaskSend{TaskID=1, numberOfFiles=3}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}
	File{name=test.txt}

TaskSend{TaskID=2, numberOfFiles=0}	[Not started]

>>> 
```

### Adding Files to A Task


```shell
>>> add
You are adding a file to a task.
Enter task ID (default: 0): 1
Enter file path (default: src/main/resources/test.jpg): src/main/resources/test.jpg
File added to task 1
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}

TaskSend{TaskID=1, numberOfFiles=4}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}
	File{name=test.txt}
	File{name=test.jpg}

TaskSend{TaskID=2, numberOfFiles=0}	[Not started]

>>> 
```

### Start a Task
```shell
>>> start
Which task do you want to start: 
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}

TaskSend{TaskID=1, numberOfFiles=4}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}
	File{name=test.txt}
	File{name=test.jpg}

TaskSend{TaskID=2, numberOfFiles=0}	[Not started]

Enter task ID: 1
Task 1 started
>>>
```

### Query the status of a Task

```shell
>>> q
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Not started]
	File{name=test.jpg}
	File{name=test.txt}

TaskSend{TaskID=1, numberOfFiles=4}	[Task Started]	[Finished]
	File{name=test.jpg, totalSegment=14, progress=14}
	File{name=test.txt, totalSegment=2, progress=2}
	File{name=test.txt, totalSegment=2, progress=2}
	File{name=test.jpg, totalSegment=14, progress=14}

TaskSend{TaskID=2, numberOfFiles=0}	[Not started]

Task-1 FileID-0: 100.00%  14/14
Task-1 FileID-1: 100.00%  2/2
Task-1 FileID-2: 100.00%  2/2
Task-1 FileID-3: 100.00%  14/14
>>> 
```

```shell
Welcome to the Receiver Console
Current Tasks:
>>> q
Current Tasks:
TaskReceive{TaskID=1, numberOfFiles=4}	[Task Started]
	File{name=test.jpg, totalSegment=14, progress=14}
	File{name=test.txt, totalSegment=2, progress=2}
	File{name=test.txt, totalSegment=2, progress=2}
	File{name=test.jpg, totalSegment=14, progress=14}

Task-1 FileID-0: 100.00%  14/14
Task-1 FileID-1: 100.00%  2/2
Task-1 FileID-2: 100.00%  2/2
Task-1 FileID-3: 100.00%  14/14
>>> q
```

### Pause and Resume a task

```shell
>>> pause
Which task do you want to pause: 
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Task Started]	[Running (Sending)]
	File{name=test.jpg, totalSegment=13871, progress=2831}
	File{name=test.txt, totalSegment=1505, progress=null}

TaskSend{TaskID=1, numberOfFiles=3}	[Task Started]	[Running (Sending)]
	File{name=test.jpg, totalSegment=13871, progress=2830}
	File{name=test.txt, totalSegment=1505, progress=null}
	File{name=test.txt, totalSegment=1505, progress=null}

Enter task ID: 0
Task 0 paused
```

```shell
>>> resume
Which task do you want to resume: 
Current Tasks:
TaskSend{TaskID=0, numberOfFiles=2}	[Task Started]	[Paused]
	File{name=test.jpg, totalSegment=13871, progress=3645}
	File{name=test.txt, totalSegment=1505, progress=null}

TaskSend{TaskID=1, numberOfFiles=3}	[Task Started]	[Running (Sending)]
	File{name=test.jpg, totalSegment=13871, progress=13871}
	File{name=test.txt, totalSegment=1505, progress=1505}
	File{name=test.txt, totalSegment=1505, progress=804}

Enter task ID: 0
Task 0 resumed
```

```shell
>>> pause
Which task do you want to pause: 
Current Tasks:
TaskReceive{TaskID=0, numberOfFiles=2}	[Task Started]
	File{name=test.jpg, totalSegment=13871, progress=10817}
	File{name=test.txt, totalSegment=1505, progress=0}

TaskReceive{TaskID=1, numberOfFiles=3}	[Task Started]
	File{name=test.jpg, totalSegment=13871, progress=13871}
	File{name=test.txt, totalSegment=1505, progress=1505}
	File{name=test.txt, totalSegment=1505, progress=1505}

Enter task ID: 1
Task 1 paused
```


```shell
>>> resume
Which task do you want to resume: 
Current Tasks:
TaskReceive{TaskID=0, numberOfFiles=2}	[Task Started]
	File{name=test.jpg, totalSegment=13871, progress=13871}
	File{name=test.txt, totalSegment=1505, progress=1505}

TaskReceive{TaskID=1, numberOfFiles=3}	[Task Started]	[Paused]
	File{name=test.jpg, totalSegment=13871, progress=13871}
	File{name=test.txt, totalSegment=1505, progress=1505}
	File{name=test.txt, totalSegment=1505, progress=1505}

Enter task ID: 1
Task 1 resumed
```
