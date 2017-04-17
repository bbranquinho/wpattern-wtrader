===INSTALLING===
once you've download the source, run
ant
in order to compile all the stuff. 

===RUNNING===
to run the test server
./run rddl.competition.Server files/boolean/rddl/

(if it still throws ClassNotFound, it's probably because I forgot to change the /bin to /build in the "run" script. making the change should fix it the error. alternatively, importing the project into eclipse should build the /bin directory automatically, so that's another option)

then, in another terminal, run the client with something like
./run rddl.competition.Client files/boolean/rddl localhost Waterloo rddl.policy.SPerseusSPUDDPolicy 2323 123456 sysp1

the line you see at the end of the server printout is the average accumulated reward per round

===MODIFYING===
there are two main parts to this project
- the rddl folder, which contains all the prepackaged competition code
- the sperseus folder, which contains Jesse Hoey's java port

the code that links them together is in rddl.policy.waterloo

a high level breakdown would look something like
-rddl.competition.Client
    -rddl.policy.SPerseusSPUDDPolicy
        -rddl.policy.waterloo.InstanceManager
            -rddl.policy.waterloo.RoundManager
                -sperseus code
the majority of the changes that I've made are in SPerseusSPUDDPolicy, sperseus.Solver and a few changes to sperseus.POMDP
I also create the wrapper classes InstanceManager and RoundManager
SPerseusSPUDDPolicy is created every time a new problem instance is activated in the Client, which then creates an InstanceManager to manage all the specifics for that instance.
A RoundManager is created for every round in that instance, reusing POMDP solutions from previous rounds.

===NOTES===
if you would like to see what's still to be done, check out kyleTODO.txt
