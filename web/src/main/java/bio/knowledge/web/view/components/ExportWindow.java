/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.web.view.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.graph.GraphManipulationInterface.SupportedFormat;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.OnDemandFileDownloader;
import bio.knowledge.web.OnDemandFileDownloader.OnDemandStreamResource;

public class ExportWindow extends Window {

	private static final long serialVersionUID = 3098666475422302951L;
	private TextField name;
	private ComboBox cb;
	private Button ok;
	private Button hidden;
	private String toExport;
	private String tsvHeader = "SubjectID	SubjectName	Relationship	ObjectID	ObjectName\n";

	public ExportWindow(KBQuery query, ConceptMapArchive map) {
		this.setId("savePopUp");
		this.setCaption("Save Map to File");
		this.setModal(true);

		VerticalLayout details = new VerticalLayout();
		details.setMargin(true);
		details.setSpacing(true);

		HorizontalLayout fileDetailsLayout = new HorizontalLayout();

		fileDetailsLayout.addStyleName("padding-bottom");

		name = new TextField();
		name.setInputPrompt("Concept map name");
		name.setId("filename");

		cb = new ComboBox();
		cb.addItems(SupportedFormat.KB.getExtension(), SupportedFormat.SIF.getExtension(),
				SupportedFormat.PNG.getExtension(), SupportedFormat.TSV.getExtension());
		cb.setId("format");
		cb.setNullSelectionAllowed(false);
		cb.select(SupportedFormat.KB.getExtension());

		fileDetailsLayout.setSpacing(true);
		fileDetailsLayout.addComponents(name, cb);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		ok = new Button("Ok");
		ok.setClickShortcut(KeyCode.ENTER);
		ok.addClickListener(e -> {

			hidden.setEnabled(true);

			fileFormat = SupportedFormat.getSupportedFormat((String) cb.getValue());

			if (fileFormat.equals(SupportedFormat.PNG)) {
				this.toExport = map.getConceptMapPng();
			} else if (fileFormat.equals(SupportedFormat.KB)) {
				this.toExport = map.getConceptMapJson();
			} else if (fileFormat.equals(SupportedFormat.SIF)) {
				this.toExport = map.getConceptMapSif();
			} else if (fileFormat.equals(SupportedFormat.TSV)) {
				this.toExport = tsvHeader + map.getConceptMapTsv();
			} else {
				throw new AssertionError("Invalid fileFormat");
			}

			String filename = name.getValue();
			String url = map.getConceptMapPng();

			if (fileFormat.equals(SupportedFormat.PNG)) {
				Page.getCurrent().getJavaScript().execute("savePng('" + filename + "', '" + url + "')");
				try {
					Thread.sleep(500);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				close();
			} else {
				Page.getCurrent().getJavaScript().execute("document.getElementById('DownloadButtonId').click();");
			}

			// close();

			// hidden.click();

			// if (format == SupportedFormat.KB) {
			// cm.exportConceptMap(SupportedFormat.getSupportedFormat((String)
			// cb.getValue()));
			// } else if (SupportedFormat.getSupportedFormat((String)
			// cb.getValue()) == SupportedFormat.SIF) {
			// exportMap(cm.convertToSIF(), SupportedFormat.SIF.getExtension());
			// } else if (SupportedFormat.getSupportedFormat((String)
			// cb.getValue()) == SupportedFormat.TSV) {
			// exportMap(cm.converterToTSV(),
			// SupportedFormat.TSV.getExtension());
			// } else if (SupportedFormat.getSupportedFormat((String)
			// cb.getValue()) == SupportedFormat.JPEG) {
			// cm.exportConceptMap(SupportedFormat.getSupportedFormat((String)
			// cb.getValue()));
			// }
		});

		Button cancel = new Button("Cancel");
		cancel.addClickListener(e -> {
			close();
		});

		hidden = new Button();
		hidden.setId("DownloadButtonId");
		hidden.setStyleName("InvisibleButton");
		// file downloader
		OnDemandStreamResource res = createOnDemandResource();
		OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(res, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				// If we close the window before the download begins, then we
				// will lose the download. This is a terrible work around, but
				// from what I can tell it's the only one available that
				// wouldn't require a great deal of work.
				Thread.sleep(500);
				close();
				return null;
			}

		});

		fileDownloader.extend(hidden);

		buttonsLayout.addComponents(ok, cancel, hidden);
		buttonsLayout.setSpacing(true);

		details.addComponents(fileDetailsLayout, buttonsLayout);
		details.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);

		details.addComponents(fileDetailsLayout, buttonsLayout);
		details.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);

		this.setResizable(false);
		this.addStyleName("popupWindow");
		this.setContent(details);
	}

	private SupportedFormat fileFormat;

	// public void exportMap(String content) {
	//
	//
	//// if (type.equals(SupportedFormat.SIF.getExtension())) {
	//// this.toExport = removeKb2Skeleton(content);
	//// } else if (type.equals(SupportedFormat.KB.getExtension())) {
	//// this.toExport = content;
	//// } else if (type.equals(SupportedFormat.TSV.getExtension())) {
	//// this.toExport = tsvHeader + content;
	//// } else if (type.equals(SupportedFormat.JPEG.getExtension())) {
	//// return; // no need to click hidden button, as it is done from
	//// // JavaScript code conceptmap.js
	//// }
	//
	// // click hidden button only when save to local machine.
	// Page.getCurrent().getJavaScript().execute("document.getElementById('DownloadButtonId').click();");
	// }

	private OnDemandStreamResource createOnDemandResource() {
		return new OnDemandStreamResource() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(toExport.getBytes());
			}

			@Override
			public String getFilename() {
				return name.getValue() + (String) cb.getValue();
			}

		};
	}
}
