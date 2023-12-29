package com.claro.amx.sp.tokenauthenticationservice.exception;


import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.claro.amx.sp.tokenauthenticationservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BusinessExceptionTest {
	private BusinessException businessException;

	@BeforeEach
	void setUp() {
		businessException = new BusinessException("Business Exception Test", "1122", "ERROR");
	}

	@Test
	void getDescriptionTest_Succes() {
		String message = "Business Exception Test";
		String code = "0000";
		try {
			throw new BusinessException(code, message, null);
		} catch (BusinessException e) {
			assertEquals(message, e.getMessage());
			assertEquals(code, e.getCode());
		}
	}

	@Test
	void getDescriptionTestSucces() {
		String message = "Business Exception Test";
		String code = "0000";
		String level = "ERROR";
		try {
			throw new BusinessException(code, message, level);
		} catch (BusinessException e) {
			assertEquals(level, e.getLevel());
			assertEquals(code, e.getCode());
		}
	}

	@Test
	void getExceptionType() {
		final ExceptionType exceptionType = businessException.getExceptionType();
		assertEquals(CUSTOM_EXCEPTION, exceptionType);
	}

	@Test
	void getExtraInfo() {
		final List<Object> list = businessException.getExtraInfo();
		assertNotNull(list);
	}

}