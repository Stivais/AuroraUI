# AuroraUI

### 2.0.0 Goal:

A *(second)* rewrite to support an OpenGL-based renderer, rather than relying on NanoVG.

It will mainly be a full rewrite to also, potentially, consider better alternatives to existing features.

### *Main* Features

State system, which should be somewhat similar to Jetpack Compose. 
Would allow for handling variables and updating a UI accordingly, without needing to do it manually in the code.

Optimized renderer (based on Aton's 2D Renderer), and rendering a UI similar to how games render terrain.

### Motivation

The reason for wanting to support opengl directly, is to have more control and much faster performance. 

Currently, with NanoVG, every frame gets re-rendered, 
and with expensive UIs, it can be decently costly *(it is not extreme, however it can be faster)*.

And it is very limiting *(mainly since lack of 3d rendering, and really hard to implement new stuff)*.