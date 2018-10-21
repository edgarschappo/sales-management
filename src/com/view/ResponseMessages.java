package com.view;

/**
 * @author edgar.luis
 */
public enum ResponseMessages {

	MESSAGE_PROCESSED {
		public String toString() {
			return "Message Processed.";
		}
	},

	FIELD_NOT_FOUND {
		public String toString() {
			return "Mandatory field not found.";
		}
	},

	MESSAGE_TYPE_NOT_FOUND {
		public String toString() {
			return "Wrong message type.";
		}
	},

	APPLICATION_PAUSED {
		public String toString() {
			return "Application is paused, try again later.";
		}
	},

	INTERNAL_ERROR {
		public String toString() {
			return "Internal server error.";
		}
	},

}
