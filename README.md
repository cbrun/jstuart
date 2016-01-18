# Introduction

Utility code to request development infrastructure (tickets, scm, reviews, continuous integration) 
and transmit events to [Mattermost](http://www.mattermost.org/)

This has been done at first to integrate the **Eclipse projects infrastructure** with the **Mattermost [Eclipse Instance](https://mattermost-test.eclipse.org/eclipse/)**

## Integrations

### RSS

!(doc/rss.png)

This can also be used for mailling list archives or stackoverflow for instance :

!(doc/stackoverflow.png)

### Git

Create a message for any new commit in the repository.

!(doc/git.png)

### Gerrit

Send aggregated reports of patchset waiting for reviews.

!(doc/gerrit.png)

### Bugzilla

Create a message for any new bug or any new comment on a bugzilla ticket.

!(doc/bugzilla.png)

### Eclipse Forums

!(doc/forums.png)

### Twitter

!(doc/twitter.png)

### Jenkins/Hudson

!(doc/jenkins.png)


## How is that supposed to be used ?

In its current incarnation it rely on the fact that you fork it and directly adapt the code for your own need.
* fork it/clone it in your own repository.
* adapt the test code to specify what to log and how. **Make sure you don't publish secret keys or tokens with your code**
* setup a Jenkins/hudson job which ```mvn clean tests``` your code regularly (every 10 minutes or so for instance)



## Why doing that in Java ? 

It was at first a set of Python scripts but it quickly became apparent that for the **Eclipse** Community
to embrace it it would need to rely on tools and technologies which are well known by this community and supported by the 
**Eclipse** infrastructure.

