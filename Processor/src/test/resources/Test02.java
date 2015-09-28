package test;

import cz.rysavi.annotations.FluentBuilder;

@FluentBuilder.Configuration
public class Test02 {
	private String field01;
	@FluentBuilder.Definition(ignoreNulls = true)
	private String field02;

	public String getField01() {
		return field01;
	}

	public void setField01(String field01) {
		this.field01 = field01;
	}

	public String getField02() {
		return field02;
	}

	public void setField02(String field02) {
		this.field02 = field02;
	}
}
