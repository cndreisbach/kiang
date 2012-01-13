#!/usr/bin/pike
// -*- mode: pike -*-
// $Id: objinst.pike,v 1.1.1.1 2004-05-19 18:11:03 bfulgham Exp $
// http://www.bagley.org/~doug/shootout/

class Toggle {
    int bool;

    object create (int start_state) {
	bool = start_state;
    }

    int value () {
	return(bool);
    }

    object activate () {
	bool = !bool;
	return(this_object());
    }
}

class NthToggle {
    inherit Toggle;

    int count_max, count;

    object create (int start_state, int max_counter) {
	::create(start_state);
	count_max = max_counter;
	count = 0;
    }

    object activate () {
	if (++count >= count_max) {
	    bool = !bool;
	    count = 0;
	}
	return(this_object());
    }
}

void main(int argc, array(string) argv) {
    int n = (int)argv[-1];
    if (n < 1) n = 1;
      
    object toggle = Toggle(1);
    for (int i=0; i<5; i++) {
	toggle->activate();
	write((toggle->value()) ? "true\n" : "false\n");
    }
    for (int i=0; i<n; i++) {
	object toggle = Toggle(1);
    }

    write("\n");

    object ntoggle = NthToggle(1, 3);
    for (int i=0; i<8; i++) {
	ntoggle->activate();
	write((ntoggle->value()) ? "true\n" : "false\n");
    }
    for (int i=0; i<n; i++) {
	object ntoggle = NthToggle(1, 3);
    }
}


