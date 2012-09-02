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
    
    private BigDecimal root(BigDecimal d) {
    	int cmp = d.compareTo(BigDecimal.ZERO);
    	if( cmp > 0 ) {
    		BigDecimal two = new BigDecimal(2);
    		BigDecimal result = d;
    		BigDecimal x = result;
    		do {
    			result = x;
    			x = x.add(d.divide(x, mMathContext));
    			x = x.divide(two, mMathContext);
    		} while( ! x.equals(result) );
    		return result;
    	} else if( cmp == 0 ) {
    		return d;
    	} else {
    		// return null for negative numbers; we don't support complex math yet
    		return null;
    	}
    }
    
    public void sqrt(View v) {
    	int i = mStack.size() - 1;
    	mMode = Mode.OP;
    	if( i >= 0 ) {
    		BigDecimal a = mStack.remove(i);
    		BigDecimal result = root(a);
    		if( result == null ) {
    			result = a;
    		}
    		mStack.add(result);
	    	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void inverse(View v) {
    	int i = mStack.size() - 1;
    	mMode = Mode.OP;
    	if( i >= 0 ) {
    		BigDecimal a = mStack.remove(i);
    		if( !a.equals(BigDecimal.ZERO) ) {
    			a = BigDecimal.ONE.divide(a, mMathContext);
    		}
    		mStack.add(a);
        	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void stats(View v) {
    	mMode = Mode.OP;
    	BigDecimal sum = BigDecimal.ZERO;
    	BigDecimal count = new BigDecimal(mStack.size());
    	for( BigDecimal d : mStack ) {
    		sum = sum.add(d);
    	}
    	BigDecimal average = sum.divide(count, mMathContext);
    	BigDecimal variance = BigDecimal.ZERO;
    	for( BigDecimal d : mStack ) {
    		BigDecimal t = average.subtract(d);
    		t = t.multiply(t);
    		variance = variance.add(t);
    	}
    	variance = variance.divide(count, mMathContext);
    	BigDecimal std_dev = root(variance);
    	mStack.add(average);
    	mStack.add(std_dev);
    	
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
    
    public void drop(View v) {
    	int i = mStack.size() - 1;
    	mMode = Mode.OP;
    	if( i >= 0 ) {
    		mStack.remove(i);
        	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void swap(View v) {
    	int i = mStack.size() - 1;
    	mMode = Mode.OP;
    	if( i > 0 ) {
    		BigDecimal a = mStack.remove(i);
    		BigDecimal b = mStack.remove(i-1);
    		mStack.add(a);
    		mStack.add(b);
        	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void dup(View v) {
    	int i = mStack.size() - 1;
    	mMode = Mode.OP;
    	if( i >= 0 ) {
    		BigDecimal a = mStack.get(i);
    		mStack.add(a);
        	mAdapter.notifyDataSetChanged();
        	mStackView.smoothScrollToPosition(mStack.size() -1);
    	}
    }
    
    public void pi(View v) {
    	mMode = Mode.OP;
    	BigDecimal pi = new BigDecimal(Math.PI);
    	mStack.add(pi);
    	mAdapter.notifyDataSetChanged();
    	mStackView.smoothScrollToPosition(mStack.size() -1);
    }
    
    
    private void d(String s) {
    	Log.d("RPN", s);
    }
    
    private BigDecimal bd(StringBuilder b) {
    	return new BigDecimal(b.toString(), mMathContext);
    }
}