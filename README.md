# Introduction

Utility code to request development infrastructure (tickets, scm, reviews, continuous integration) 
and transmit events to [Mattermost](http://www.mattermost.org/)

This has been done at first to integrate the **Eclipse projects infrastructure** with the **Mattermost [Eclipse Instance](https://mattermost-test.eclipse.org/eclipse/)** but can be used for your on premise instance of Mattermost and can be useful especially if you are a Java shop.

## Integrations

### RSS

![RSS integration](doc/rss.png)

This can also be used for mailling list archives or stackoverflow for instance :

![RSS integration used for StackOverflow](doc/stackoverflow.png)

### Git

Create a message for any new commit in the repository.

![GIT integration](doc/git.png)

### Gerrit

Send aggregated reports of patchset waiting for reviews.

![Gerrit integration](doc/gerrit.png)

### Bugzilla

Create a message for any new bug or any new comment on a bugzilla ticket.

![Bugzilla Integration](doc/bugzilla.png)

### Eclipse Forums

![Eclipse Forums -- tweaked RSS](doc/forums.png)

### Twitter

![Twitter](doc/twitter.png)

### Jenkins/Hudson

![Jenkins](doc/jenkins.png)

## Modules

### Shared tasks

The bot provides a shared tasks utility, backed by a Google Spreadsheet.
The goal is to allow users to create tasks in a channel, and any user of that channel may register themselves as volunteers for tasks. Any user may declare that a particular task needs to be done, in which case the bot assigns the task using a fair distribution algorithm.
To use this module, the ID of a publicly-accessible Google Spreadsheet **must** be provided in the arguments when starting the bot.


## How is that supposed to be used ?

In its current incarnation it rely on the fact that you fork it and directly adapt the code for your own need.
* fork it/clone it in your own repository.
* adapt the test code to specify what to log and how. **Make sure you don't publish secret keys or tokens with your code**
* setup a Jenkins/hudson job which ```mvn clean tests``` your code regularly (every 10 minutes or so for instance)



## Why doing that in Java ? 

It was at first a set of Python scripts but it quickly became apparent that for the **Eclipse** Community
to embrace it it would need to rely on tools and technologies which are well known by this community and supported by the 
**Eclipse** infrastructure.

