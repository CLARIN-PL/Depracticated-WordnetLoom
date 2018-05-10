# WordnetLoom Visual Editor

WordnetLoom application is a application to build your wordnets in easy visual way.
This branch contains only Princeton Wordnet 3.1

## Server application
Installation dependencies (links for ubuntu)
  - git 
        `sudo apt-get install git`
  - installed java 8 jdk 
        `sudo add-apt-repository ppa:webupd8team/java`
        `sudo apt-get update`
        `sudo apt-get install oracle-java8-installer`
  - installed Maven 3+ 
        `sudo apt-get install maven`
  - [docker](https://docs.docker.com/install/)
  - [docker-compose](https://docs.docker.com/compose/install/)

  `git clone git@github.com:CLARIN-PL/WordnetLoom.git`
  `cd WordnetLoom/`
  `git checkout princeton`
   Run build script  `./build.sh`

## Client application
- client machine requires installed java jre 8+ 
Go to `client` folder and run  start.sh or start.bat
Default configuration is set to localhost but you can change it by setting up 
correct value for WORDNETLOOM_SERVER_HOST={your server ip} in start.* file