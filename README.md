Chemistry Demos
===============

These Chemistry Demos were developed in the Summer of 2007 as the result of a
part-time position under Professor Geissler.  It was my first programming job,
which I had received after only having completed one semester of CS.  Though,
the code looks pretty bad by my modern standards, I'm still impressed with the
results.

So here they are, my chemistry demos, warts and all.

[***GIFS BELOW***][gifs_url]

Running the Demos
-----------------

[Gradle][gradle_url] is used to manage the code.

```bash
# download the open dependencies as they haven't been published yet
# fixes a bug
make
```

```bash
# display all posible gradle tasks
gradle tasks --all

# compile, create the jar, and execute
gradle spheres:run
gradle inkdrop:run
gradle stretch:run
```

Currently, the OpenGL native library dependencies are specified to be OSX.
If you are trying to run this code on Linux or Windows please update the
dependencies in build.gradle.

```groovy
// TODO Change if necessary.
compile group: 'org.jogamp.gluegen', name: 'gluegen-rt', version: '2.2.4', classifier: 'natives-macosx-universal'
compile group: 'org.jogamp.jogl',    name: 'jogl-all',   version: '2.2.4', classifier: 'natives-macosx-universal'
```


Sphere Collisions
-----------------

The demonstration is configurable, but what you see in the animation below is a single sphere with a specified
amount of energy, launching into its neighbor spheres, and spreading energy throughout the system.  The main
thing to note is that the distribution eventually settles into the one predicted.  Also, it is pretty addictive
to watch all of the collisions.

Another cool thing about this demo is how collisions are being calculated.  Typically, collision detection works
by updating the positions of the objects and then checking if any overlap has occurred.  If there is overlap then
the objects' positions are backtracked and then repositioned.  The problem with this system is that fast moving
objects can potentially move so fast that overlap is never seen.  My system works by calculating all of the potential
collisions mathematically and keeping the order of these collisions in order by time.  The simulation then jumps
to the next collision, updates the particles positions, and invalidates other affected collisions.

![Sphere Collisions GIF][spheres_gif]


Inkdrop Dispersion
------------------

This demonstration shows how liquid dispersion happens via simple Brownian motion.  In the demo you can also see
me added additional particles.  The visual trace mode is also neat to watch individual particles move about.

![Inkdrop Dispersion GIF][inkdrop_gif]


Polymer Stretching (BUGGY)
-------------------------------

This visualization is intended to show Polymer motion as a spring system.  In the image below a piece of the polymer
is moving to the right at a fixed velocity and the rest of the Polymer is acting accordingly.  It is a bit buggy with
fast stretching.


[gifs_url]: https://github.com/esiegel/chem_demos#sphere-collisions
[gradle_url]: http://www.gradle.org/
[spheres_gif]: ../media/spheres.gif?raw=true
[inkdrop_gif]: ../media/inkdrop.gif?raw=true
