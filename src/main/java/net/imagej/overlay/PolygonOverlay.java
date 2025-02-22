/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2020 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.overlay;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.imagej.axis.Axes;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.PolygonRegionOfInterest;

import org.scijava.Context;

/**
 * TODO
 * 
 * @author Lee Kamentsky
 */
public class PolygonOverlay extends
	AbstractROIOverlay<PolygonRegionOfInterest>
{
	// default constructor for use by serialization code
	//   (see AbstractOverlay::duplicate())
	public PolygonOverlay() {
		super(new PolygonRegionOfInterest());
	}
	
	public PolygonOverlay(final Context context) {
		super(context, new PolygonRegionOfInterest());
		this.setAxis(new DefaultLinearAxis(Axes.X), 0);
		this.setAxis(new DefaultLinearAxis(Axes.Y), 1);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		final PolygonRegionOfInterest roi = getRegionOfInterest();
		final int vertexCount = roi.getVertexCount();
		out.writeInt(vertexCount);
		for (int i = 0; i < vertexCount; i++) {
			final RealLocalizable vertex = roi.getVertex(i);
			out.writeDouble(vertex.getDoublePosition(0));
			out.writeDouble(vertex.getDoublePosition(1));
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException,
		ClassNotFoundException
	{
		super.readExternal(in);
		final PolygonRegionOfInterest roi = getRegionOfInterest();
		while (roi.getVertexCount() > 0) {
			roi.removeVertex(0);
		}
		final int vertexCount = in.readInt();
		for (int i = 0; i < vertexCount; i++) {
			final RealPoint vertex =
				new RealPoint(new double[] { in.readDouble(), in.readDouble() });
			roi.addVertex(i, vertex);
		}
	}

	@Override
	public void move(double[] deltas) {
		getRegionOfInterest().move(deltas);
	}

}
