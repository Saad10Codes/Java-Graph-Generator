package com.sample.calclate;

public class Calculate {

	public static final int CODE_ADD = 0;
	public static final int CODE_SUB = 1;
	public static final int CODE_MULTI = 2;
	public static final int CODE_DIV = 3;
	int code;
	int x;
	int y;

	public Calculate(int x, int y, int code) {
		setCode(code);
		setX(x);
		setY(y);
	}

	public int add(int x, int y) {
		return x + y;
	}

	public int sub(int x, int y) {
		return 0;
	}
	
	public int multi(int x, int y) {
		return 0;
	}
	
	public int div(int x, int y) {
		return 0;
	}
	
	public int exec() {
		int result = 0;
		switch (code) {
		case CODE_ADD:
			result = add(x, y);
			break;

		default:
			break;
		}
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
