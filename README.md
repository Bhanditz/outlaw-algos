# Outlaw Algorithms
Bandit Algorithms in Clojure

This lib is meant to be used from other JVM languages.

Here is an example how to use it from Java:

```java
import java.util.Random;
import outlawalgos.*;

public class Outlaw {
    public static void main(String[] args) {
	int arm = 0;
	Random r = new Random();
	
	MultiarmedBandit b = new MultiarmedBandit(0.1, 5);
	for (int i=0; i < 10; i++) {
	    arm = b.selectArm();
	    double reward = r.nextDouble() > 0.9 ? 0.0:1.0;
	    b.updateReward(arm, reward);
	    System.out.println(b);
	}	
	System.exit(0);
    }
}
```
Try it:

Check out the code. cd into outlaw-algos.

Generate uberjar, which contains all that is needed to run this standalone.

```bash
~/proj/outlaw-algos[master⚡]$ lein uberjar
```
```
Compiling outlaw-algos.core
Created /Users/stefan/proj/outlaw-algos/target/outlaw-algos-0.1.0-SNAPSHOT.jar
Created /Users/stefan/proj/outlaw-algos/target/outlaw-algos-0.1.0-SNAPSHOT-standalone.jar
```

Compile the Java class example.

```bash
~/proj/outlaw-algos[master⚡]$ cd examples
~/proj/outlaw-algos/examples[master⚡]$ javac -cp ../target/outlaw-algos-0.1.0-SNAPSHOT-standalone.jar Outlaw.java 
```
Now run it.

```bash
~/proj/outlaw-algos/examples[master⚡]$ java -cp ../target/outlaw-algos-0.1.0-SNAPSHOT-standalone.jar Outlaw 
```

