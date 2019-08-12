package com.anconet.gengrpr.validation;

import org.apache.commons.cli.CommandLine;

public interface IInputValidator {
	boolean validate(CommandLine commandLine);
}
