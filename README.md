# JOGLObjTest

### What is this?
Simple OpenGL app I wrote for testing [JOGL](https://jogamp.org/jogl/www/).
You can load a Wavefront .obj file and render it.
Use the following keys to control:
- w, s, a, d, q, e: translate
- j, k, l, i, u, o: rotate
- +, -: scale

### Dependencies
- [JOGL](https://jogamp.org/jogl/www/) (Version I used: 2.3)
- [Gluegen](http://jogamp.org/gluegen/www/) (included in JOGL distribution)

### Remarks
- Since this app is just for testing, I did not implement all features
of .obj file. It is recommended to use simple .obj files (without material, etc.).

- Since JOGL uses native libraries (through Gluegen and JNI),
you should use these files to build this app (and general JOGL apps):
  - ```jogl-all.jar```
  - ```jogl-all-natives-(your OS)-(your architecture).jar```
  - ```gluegen-rt.jar```
  - ```gluegen-rt-natives-(your OS)-(your architecture).jar```
  
  In ```lib``` folder, ```.jar``` files for Windows & x64 are included.
  (For detail, see [here](https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL)
  and [here](https://jogamp.org/wiki/index.php/Setting_up_a_JogAmp_project_in_your_favorite_IDE).)

### Screenshots
![Screenshot](http://i.imgur.com/tS961jW.png)
