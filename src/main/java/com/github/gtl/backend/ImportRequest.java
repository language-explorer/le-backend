package com.github.gtl.backend;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ImportRequest {
	@NotBlank
	private String path;
}
