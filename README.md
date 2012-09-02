# RPN Calculator for Android #

This project is a simple RPN calculator for Android; written mostly because I wanted to write something useful for my phone, and good RPN calculators are hard to come by these days.

There are probably better options out there, but I'm too lazy to look for them.

It's based on Java's BigDecimal class, with 128-bit precision.

The current Android API is set to 2.3.3; it may compile and work on older devices, but I haven't tried it.

It's a work in progress. I might put it up on the android store some day, but there are a lot of things to do first.

# TODO #
 * Save and restore state properly
 * Update the buttons so that they don't look horrible
 * Add more functions!
  * Math functions:
   * sin, cos, tan, etc
   * Arbitrary powers
 * Right-align or decimal-align the stack
  * Decimal-align will look better, but be harder
 * Test on various screen sizes and devices
 * Deal with screen rotation properly
  * preserve the stack, input mode and input temporary
  * probably override onSaveInstanceState or some such
  * Possible alternate layouts for landscape and portrait
 * Drag to re-arrange the stack?
  * This would be a fun new twist on the old RPN calculator style
 * Specify precision
  * Add an inteface to specify the MathContext

# Out of Scope #
There are a few things that are out of the scope of this project, either for time or complexity reasons

 * Graphing: the UI and the engine are different enough that this should be a separate app.
 * Symbolic solving: I don't have the time or the know-how to reinvent Mathmatica
 * Complex Numbers: this would require changing the internal data type, and I don't really want to open that can of worms right now.
