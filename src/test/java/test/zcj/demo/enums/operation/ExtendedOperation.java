package test.zcj.demo.enums.operation;

public enum ExtendedOperation implements Operation {
	// 求x的y次幂
	EXP("^") {
		@Override
		public double apply(double x, double y) {
			return Math.pow(x, y);
		}
	},
	// 求x模y的值
	REMAINDER("%") {
		@Override
		public double apply(double x, double y) {
			return x % y;
		}
	};

	private final String symbol;

	ExtendedOperation(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return symbol;
	}
}