/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gagravarr.vorbis;

import org.gagravarr.ogg.IOUtils;
import org.gagravarr.ogg.OggPacket;
import org.gagravarr.ogg.HighLevelOggStreamPacket;

/**
 * Parent of all Vorbis packets
 */
public abstract class VorbisPacket extends HighLevelOggStreamPacket {
   public static final int TYPE_INFO = 1;
   public static final int TYPE_COMMENTS = 3;
   public static final int TYPE_SETUP = 5;
   
   protected static final int HEADER_LENGTH_METADATA = 7;
   protected static final int HEADER_LENGTH_AUDIO = 0;
   
	protected VorbisPacket(OggPacket oggPacket) {
	   super(oggPacket);
	}
	protected VorbisPacket() {
	   super();
	}
	
	/**
	 * How big is the header on this packet?
	 * For Metadata packets it's normally 7 bytes,
	 *  otherwise for audio packets there is no header.
	 */
	protected abstract int getHeaderSize();
	
	/**
	 * Popupulates the metadata packet header,
	 *  which is "#vorbis" where # is the type.
	 */
	protected void populateMetadataHeader(byte[] b, int type, int dataLength) {
		b[0] = IOUtils.fromInt(type);
		b[1] = (byte)'v';
		b[2] = (byte)'o';
		b[3] = (byte)'r';
		b[4] = (byte)'b';
		b[5] = (byte)'i';
		b[6] = (byte)'s';
	}
	
	/**
	 * Does this packet (the first in the stream) contain
	 *  the magic string indicating that it's a vorbis
	 *  one?
	 */
	public static boolean isVorbisStream(OggPacket firstPacket) {
		if(! firstPacket.isBeginningOfStream()) {
			return false;
		}
		return isVorbisSpecial(firstPacket);
	}
	
	private static boolean isVorbisSpecial(OggPacket packet) {
		byte type = packet.getData()[0];
		
		// Ensure "vorbis" on the special types
		if(type == 1 || type == 3 || type == 5) {
			byte[] d = packet.getData();
			if(d[1] == (byte)'v' &&
				d[2] == (byte)'o' &&
				d[3] == (byte)'r' &&
				d[4] == (byte)'b' &&
				d[5] == (byte)'i' &&
				d[6] == (byte)'s') {
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates the appropriate {@link VorbisPacket}
	 *  instance based on the type.
	 */
	public static VorbisPacket create(OggPacket packet) {
		byte type = packet.getData()[0];
		
		// Special header types detection
		if(isVorbisSpecial(packet)) {
			switch(type) {
			case TYPE_INFO:
				return new VorbisInfo(packet);
			case TYPE_COMMENTS:
				return new VorbisComments(packet);
			case TYPE_SETUP:
				return new VorbisSetup(packet);
			}
//		} else {
//			throw new IllegalArgumentException(
//					"Magic string 'vorbis' not found for packet of type " + type +
//					" - first few bytes are: " +
//					Integer.toHexString(IOUtils.toInt(d[1])) + " " +
//					Integer.toHexString(IOUtils.toInt(d[2])) + " " +
//					Integer.toHexString(IOUtils.toInt(d[3])) + " " +
//					Integer.toHexString(IOUtils.toInt(d[4])) + " "
//			);
		}
		
		return new VorbisAudioData(packet);
	}
}