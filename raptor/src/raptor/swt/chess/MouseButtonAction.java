/**
 * New BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 * Copyright (c) 2010, RaptorProject (http://code.google.com/p/raptor-chess-interface/)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the RaptorProject nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package raptor.swt.chess;

import org.eclipse.swt.events.MouseEvent;

import raptor.pref.PreferenceKeys;

public enum MouseButtonAction {
	Left, Middle, Right, Misc1, Misc2, LeftDoubleClick;

	public static MouseButtonAction buttonFromEvent(MouseEvent e) {
		switch (e.button) {
		case 1:
			return Left;
		case 2:
			return Middle;
		case 3:
			return Right;
		case 4:
			return Misc1;
		case 5:
			return Misc2;
		default:
			return null;
		}
	}

	public String getPreferenceSuffix() {
		switch (this) {
		case Left:
			return PreferenceKeys.LEFT_MOUSE_BUTTON_ACTION;
		case Right:
			return PreferenceKeys.RIGHT_MOUSE_BUTTON_ACTION;
		case Middle:
			return PreferenceKeys.MIDDLE_MOUSE_BUTTON_ACTION;
		case Misc1:
			return PreferenceKeys.MISC1_MOUSE_BUTTON_ACTION;
		case Misc2:
			return PreferenceKeys.MISC2_MOUSE_BUTTON_ACTION;
		case LeftDoubleClick:
			return PreferenceKeys.LEFT_DOUBLE_CLICK_MOUSE_BUTTON_ACTION;
		default:
			return null;
		}
	}
}
