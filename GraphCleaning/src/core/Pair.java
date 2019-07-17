package core;

public class Pair<L, R> {
	  L key;
	  R value;

	  public Pair(L left, R right) {
	    this.key = left;
	    this.value = right;
	  }

	public L getKey() {
		return key;
	}
	
	public R getValue() {
		return value;
	}
}
