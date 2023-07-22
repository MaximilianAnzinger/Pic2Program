package de.p2l.service.parser.parser;

import de.p2l.service.classifier.Result;

/*
 * Enums for possible inputs
 */
public enum Input implements Result {
	FORWARD, BACKWARD, LEFTTURN, RIGHTTURN, INTERACT, BRANCHKEY, LOOPKEY, BLOCKEND, BLOCKEDIN, FREEIN, GOALIN, NOGOALIN;
}
