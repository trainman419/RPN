package com.namniart.rpn;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RPNCalculatorActivity extends Activity {
	private enum Mode {
		// TODO: figure out if we need any other modes,
		//  or reduce this to a boolean
		INPUT, OP
	}

	private List<Double> mStack; // the stack
	private Mode mMode;    // input mode; are we in input mode?
	private StringBuilder mInput; // string version of current input
	
	private ArrayAdapter<Double> mAdapter;
	private ListView mStackView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStack = new LinkedList<Double>();
        mMode = Mode.INPUT;
        mInput = new StringBuilder();
        
        mStack.add(0.0);
        
        mStackView = (ListView) findViewById(R.id.stack);
        mAdapter = new ArrayAdapter<Double>(this,
        		R.layout.stack_item, mStack);
        mStackView.setAdapter(mAdapter);
    }
        
    public void number(View v) {
    	if( v instanceof Button ) {
    		Button b = (Button)v;
    		d("Got number " + b.getText());
    		if( mMode == Mode.INPUT ) {
        		mInput.append(b.getText());
    			mStack.set(mStack.size()-1, Double.parseDouble(new String(mInput)));
    		} else {
    			mInput = new StringBuilder();
        		mInput.append(b.getText());
    			mStack.add(Double.parseDouble(new String(mInput)));
    			mMode = Mode.INPUT;
    		}
    		d("mInput: " + mInput);
    		mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void add(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	Double a = mStack.remove(i);
	    	Double b = mStack.remove(i-1);
	    	mStack.add(a + b);
			mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void subtract(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	Double a = mStack.remove(i);
	    	Double b = mStack.remove(i-1);
	    	mStack.add(b - a);
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void div(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	Double a = mStack.remove(i);
	    	Double b = mStack.remove(i-1);
	    	mStack.add(b / a);
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void multiply(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	Double a = mStack.remove(i);
	    	Double b = mStack.remove(i-1);
	    	mStack.add(a * b);
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void sign(View v) {
    	// always change the sign of the bottom item on the stack
    	if( mMode == Mode.INPUT ) {
    		// prepend a negative sign
    		mInput.insert(0, '-');
    	}
    	int i = mStack.size() - 1;
    	mStack.set(i, - mStack.get(i));
    	mAdapter.notifyDataSetChanged();
    	mStackView.smoothScrollToPosition(mStack.size() -1);
    }
    
    public void dot(View v) {
    	// only append a dot if our string doesn't contain one
    	if( mInput.indexOf(".") == -1 ) {
    		if( mMode == Mode.INPUT ) {
    			if( mInput.length() == 0 ) {
    				mInput.append('0');
    			}
    			mInput.append('.');
    			mStack.set(mStack.size()-1, Double.parseDouble(new String(mInput)));
    		} else {
    			mInput = new StringBuilder();
    			mInput.append('0');
    			mInput.append('.');
    			mStack.add(Double.parseDouble(new String(mInput)));
    			mMode = Mode.INPUT;
    		}
    		mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void enter(View v) {
    	mMode = Mode.OP;
    	mInput = new StringBuilder();
    }
    
    public void clear(View v) {
    	mMode = Mode.INPUT;
    	mStack.clear();
    	mStack.add(0.0);
    	mInput = new StringBuilder();
    	mAdapter.notifyDataSetChanged();
    	mStackView.smoothScrollToPosition(0);
    }
    
    private void d(String s) {
    	Log.d("RPN", s);
    }
}