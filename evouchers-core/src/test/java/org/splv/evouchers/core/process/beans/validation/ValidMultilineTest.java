package org.splv.evouchers.core.process.beans.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.AllArgsConstructor;
import lombok.Data;

class ValidMultilineTest {

	private static Validator validator;
	
	@BeforeAll
	static void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Data
	@AllArgsConstructor
	private class TestClass {
		@MultilineConstraint(maxLengthPerLine = 10, maxLines = 3)
		private String s1;
		@MultilineConstraint(minLengthPerLine = 10)
		private String s2;
		
	}
	
	@ParameterizedTest
	@MethodSource("provideValidationCandidates")
	void test(String s1, String s2, int expectedViolationNumberS1, int expectedViolationNumberS2) {
		var bean = new TestClass(s1, s2);
		Set<ConstraintViolation<TestClass>> violations = validator.validate(bean);
		long nbViolationsS1 = violations.stream().filter(v -> "s1".equals(v.getPropertyPath().toString())).count();
		long nbViolationsS2 = violations.stream().filter(v -> "s2".equals(v.getPropertyPath().toString())).count();
		assertEquals(expectedViolationNumberS1, nbViolationsS1);
		assertEquals(expectedViolationNumberS2, nbViolationsS2);
	}
	
	private static Stream<Arguments> provideValidationCandidates() {
		return Stream.of(
				Arguments.of("", "", 0, 1),
				Arguments.of("", "1234567890", 0, 0),
				Arguments.of("12345678901", "", 1, 1),
				Arguments.of("123\n123\n123\n123", "1234567890", 1, 0),
				Arguments.of(null, null, 0, 1)
				);
	}
}
