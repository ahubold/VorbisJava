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

import org.gagravarr.ogg.OggPacket;

/**
 * Raw, compressed audio data
 */
public class VorbisAudioData extends VorbisPacket {
	private long granulePosition;
	
	public VorbisAudioData(OggPacket pkt) {
		super(pkt);
		granulePosition = pkt.getGranulePosition();
	}
	public VorbisAudioData(byte[] data) {
		super();
		setData(data);
		granulePosition = -1;
	}
	
	protected int getHeaderSize() {
	   return 0;
	}

	/**
	 * Returns the granule position, or -1 if
	 *  this wasn't read from a file
	 */
	public long getGranulePosition() {
		return granulePosition;
	}
	public void setGranulePosition(long granulePosition) {
		this.granulePosition = granulePosition;
	}
}
