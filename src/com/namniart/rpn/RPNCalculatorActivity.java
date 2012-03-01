package com.namniart.rpn;

import java.math.BigDecimal;
import java.math.MathContext;
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

	private List<BigDecimal> mStack; // the stack
	private Mode mMode;    // input mode; are we in input mode?
	private StringBuilder mInput; // string version of current input
	
	private ArrayAdapter<BigDecimal> mAdapter;
	private ListView mStackView;
	private MathContext mMathContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStack = new LinkedList<BigDecimal>();
        mMode = Mode.INPUT;
        mInput = new StringBuilder();
        mMathContext = MathContext.DECIMAL128;
        
        mStack.add(BigDecimal.ZERO);
        
        mStackView = (ListView) findViewById(R.id.stack);
        mAdapter = new ArrayAdapter<BigDecimal>(this,
        		R.layout.stack_item, mStack);
        mStackView.setAdapter(mAdapter);
    }
        
    public void number(View v) {
    	if( v instanceof Button ) {
    		Button b = (Button)v;
    		d("Got number " + b.getText());
    		if( mMode == Mode.INPUT ) {
        		mInput.append(b.getText());
    			mStack.set(mStack.size()-1, bd(mInput));
    		} else {
    			mInput = new StringBuilder();
        		mInput.append(b.getText());
    			mStack.add(bd(mInput));
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
	    	BigDecimal a = mStack.remove(i);
	    	BigDecimal b = mStack.remove(i-1);
	    	mStack.add(a.add(b));
			mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void subtract(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	BigDecimal a = mStack.remove(i);
	    	BigDecimal b = mStack.remove(i-1);
	    	mStack.add(b.subtract(a));
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void div(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	BigDecimal a = mStack.remove(i);
	    	BigDecimal b = mStack.remove(i-1);
	    	mStack.add(b.divide(a, mMathContext));
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void multiply(View v) {
    	int i = mStack.size() - 1;
		mMode = Mode.OP;
		if( i > 0 ) {
	    	BigDecimal a = mStack.remove(i);
	    	BigDecimal b = mStack.remove(i-1);
	    	mStack.add(a.multiply(b));
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
		}
    }
    
    public void sign(View v) {
    	// always change the sign of the bottom item on the stack
    	if( mMode == Mode.INPUT ) {
    		int i = mInput.indexOf("-");
    		// if there isn't a negative sign, add one. else, remove it
    		if(-1 == i) {
        		// prepend a negative sign
    			mInput.insert(0, '-');
    		} else {
    			// delete the negative sign
    			mInput.deleteCharAt(i);
    		}
    	}
    	int i = mStack.size() - 1;
    	mStack.set(i, mStack.get(i).negate());
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
    			mStack.set(mStack.size()-1, bd(mInput));
    		} else {
    			mInput = new StringBuilder();
    			mInput.append('0');
    			mInput.append('.');
    			mStack.add(bd(mInput));
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
    	mStack.add(BigDecimal.ZERO);
    	mInput = new StringBuilder();
    	mAdapter.notifyDataSetChanged();
    	mStackView.smoothScrollToPosition(0);
    }
    
    private void d(String s) {
    	Log.d("RPN", s);
    }
    
    private BigDecimal bd(StringBuilder b) {
    	return new BigDecimal(b.toString(), mMathContext);
    }
}