# RPN Calculator for Android #

This project is a simple RPN calculator for Android; written mostly because I wanted to write something useful for my phone, and good RPN calculators are hard to come by these days.

There are probably better options out there, but I'm too lazy to look for them.

It's based on double-precision arithmetic, so any rounding errors or other oddities present in double-precicsion numbers will show up.

The current Android API is set to 2.3.3; it may compile and work on older devices, but I haven't tried it.

It's a work in progress. I might put it up on the android store some day, but there are a lot of things to do first.

# TODO #
 * Fix the layout so that it scrolls properly when the stack is large
 * Update the buttons so that they don't look horrible
 * Add more functions!
  * Swap
  * Drop item
  * Math functions:
   * sin, cos, tan, etc
   * inverse
   * square root
   * Arbitrary powers
 * Specify precision
 * Right-align or decimal-align the stack
  * Decimal-align will look better, but be harder
  * Decimal-align won't deal well with exponential notation
 * Test on various screen sizes and devices
 * Deal with screen rotation properly
  * preserve the stack, input mode and input temporary
  * probably override onSaveInstanceState or some such
  * Possible alternate layouts for landscape and portrait
 * PI
 * Drag to re-arrange the stack?
  * This would be a fun new twist on the old RPN calculator style

# Out of Scope #
There are a few things that are out of the scope of this project, either for time or complexity reasons
 * Graphing: the UI and the engine are different enough that this should be a separate app.
 * Symbolic solving: I don't have the time or the know-how to reinvent Mathmatica
