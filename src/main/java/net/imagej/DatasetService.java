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

package net.imagej;

import java.io.IOException;
import java.util.List;

import net.imagej.axis.AxisType;
import net.imagej.display.ImageDisplay;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;

import org.scijava.object.ObjectService;

/**
 * Interface for service that works with {@link Dataset}s.
 * 
 * @author Curtis Rueden
 * @author Mark Hiner
 */
public interface DatasetService extends ImageJService {

	ObjectService getObjectService();

	/**
	 * Gets a list of all {@link Dataset}s. This method is a shortcut that
	 * delegates to {@link ObjectService}.
	 */
	List<Dataset> getDatasets();

	/**
	 * Gets a list of {@link Dataset}s linked to the given {@link ImageDisplay}.
	 */
	List<Dataset> getDatasets(ImageDisplay display);

	/**
	 * Creates a new dataset.
	 * 
	 * @param dims The dataset's dimensional extents.
	 * @param name The dataset's name.
	 * @param axes The dataset's dimensional axis labels.
	 * @param bitsPerPixel The dataset's bit depth. Currently supported bit depths
	 *          include 1, 8, 12, 16, 32 and 64.
	 * @param signed Whether the dataset's pixels can have negative values.
	 * @param floating Whether the dataset's pixels can have non-integer values.
	 * @return The newly created dataset.
	 * @throws IllegalArgumentException If the combination of bitsPerPixel, signed
	 *           and floating parameters do not form a valid data type.
	 */
	Dataset create(long[] dims, String name, AxisType[] axes, int bitsPerPixel,
		boolean signed, boolean floating);

	/**
	 * Creates a new dataset.
	 * 
	 * @param dims The dataset's dimensional extents.
	 * @param name The dataset's name.
	 * @param axes The dataset's dimensional axis labels.
	 * @param bitsPerPixel The dataset's bit depth. Currently supported bit depths
	 *          include 1, 8, 12, 16, 32 and 64.
	 * @param signed Whether the dataset's pixels can have negative values.
	 * @param floating Whether the dataset's pixels can have non-integer values.
	 * @param virtual Whether to store in a virtual data structure or not.
	 * @return The newly created dataset.
	 * @throws IllegalArgumentException If the combination of bitsPerPixel, signed
	 *           and floating parameters do not form a valid data type.
	 */
	Dataset create(long[] dims, String name, AxisType[] axes, int bitsPerPixel,
		boolean signed, boolean floating, boolean virtual);

	/**
	 * Creates a new dataset.
	 * 
	 * @param <T> The type of the dataset.
	 * @param type The type of the dataset.
	 * @param dims The dataset's dimensional extents.
	 * @param name The dataset's name.
	 * @param axes The dataset's dimensional axis labels.
	 * @return The newly created dataset.
	 */
	<T extends RealType<T> & NativeType<T>> Dataset create(T type, long[] dims,
		String name, AxisType[] axes);

	/**
	 * Creates a new dataset.
	 * 
	 * @param <T> The type of the dataset.
	 * @param type The type of the dataset.
	 * @param dims The dataset's dimensional extents.
	 * @param name The dataset's name.
	 * @param axes The dataset's dimensional axis labels.
	 * @param virtual If true make a virtual dataset.
	 * @return The newly created dataset.
	 */
	<T extends RealType<T> & NativeType<T>> Dataset create(T type, long[] dims,
		String name, AxisType[] axes, boolean virtual);

	/**
	 * Creates a new dataset using the provided {@link ImgFactory}.
	 * 
	 * @param <T> The type of the dataset.
	 * @param factory The ImgFactory to use to create the data.
	 * @param dims The dataset's dimensional extents.
	 * @param name The dataset's name.
	 * @param axes The dataset's dimensional axis labels.
	 * @return The newly created dataset.
	 */
	<T extends RealType<T>> Dataset create(
		ImgFactory<T> factory, long[] dims, String name, AxisType[] axes);

	/**
	 * Creates a new dataset using the provided {@link ImgPlus}.
	 * 
	 * @param imgPlus The {@link ImgPlus} backing the dataset.
	 * @return The newly created dataset.
	 */
	<T extends Type<T>> Dataset create(ImgPlus<T> imgPlus);

	/**
	 * Creates a new dataset using the provided {@link RandomAccessibleInterval}.
	 * 
	 * @param rai The {@link RandomAccessibleInterval}, which will be wrapped into
	 *          an {@link ImgPlus} that will back the new dataset.
	 * @return The newly created dataset.
	 */
	<T extends Type<T>> Dataset create(RandomAccessibleInterval<T> rai);

	/**
	 * Determines whether the given source can be opened as a {@link Dataset}
	 * using the {@link #open(String)} method.
	 * 
	 * @deprecated Use io.scif.services.DatasetIOService#canOpen instead.
	 */
	@Deprecated
	boolean canOpen(String source);

	/**
	 * Determines whether the given destination can be used to save a
	 * {@link Dataset} using the {@link #save(Dataset, String)} method.
	 * 
	 * @deprecated Use io.scif.services.DatasetIOService#canSave instead.
	 */
	@Deprecated
	boolean canSave(String destination);

	/**
	 * Loads a dataset from a source (such as a file on disk).
	 * 
	 * @deprecated Use io.scif.services.DatasetIOService#open instead.
	 */
	@Deprecated
	Dataset open(String source) throws IOException;

	/**
	 * As {@link #open(String)}, with the given
	 * {@code io.scif.config.SCIFIOConfig}.
	 * 
	 * @deprecated Use io.scif.services.DatasetIOService#open instead.
	 */
	@Deprecated
	Dataset open(String source, Object config) throws IOException;

	/**
	 * Reverts the given dataset to its original source.
	 * 
	 * @deprecated Use io.scif.services.DatasetIOService#revert instead.
	 */
	@Deprecated
	void revert(Dataset dataset) throws IOException;

	/**
	 * Saves a dataset to a destination (such as a file on disk).
	 * 
	 * @param dataset The dataset to save.
	 * @param destination Where the dataset should be saved (e.g., a file path on
	 *          disk).
	 * @deprecated Use io.scif.services.DatasetIOService#save instead.
	 */
	@Deprecated
	Object save(Dataset dataset, String destination) throws IOException;

	/**
	 * Saves a dataset to a destination (such as a file on disk).
	 * 
	 * @param dataset The dataset to save.
	 * @param destination Where the dataset should be saved (e.g., a file path on
	 *          disk).
	 * @param config The {@code io.scif.config.SCIFIOConfig} describing how the
	 *          data should be saved.
	 * @deprecated Use io.scif.services.DatasetIOService#save instead.
	 */
	@Deprecated
	Object save(Dataset dataset, String destination, Object config)
		throws IOException;

	/**
	 * @deprecated Use {@link #create(ImgFactory, long[], String, AxisType[])}
	 *             instead.
	 */
	@Deprecated
	<T extends RealType<T>> Dataset create(ImgFactory<T> factory, T type,
		long[] dims, String name, AxisType[] axes);
}
