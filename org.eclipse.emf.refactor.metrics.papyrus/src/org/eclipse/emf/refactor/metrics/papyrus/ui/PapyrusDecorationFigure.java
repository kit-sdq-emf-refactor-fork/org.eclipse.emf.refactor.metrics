package org.eclipse.emf.refactor.metrics.papyrus.ui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

public class PapyrusDecorationFigure extends Figure {
	
//	private static final Color NICE_GREEN = new Color(null, 72, 232, 232);
	private static final Color NICE_GREEN = new Color(null, 77, 188, 77);

	public PapyrusDecorationFigure(){
		setLayoutManager(new ToolbarLayout());
		setOpaque(false);
	}	
	
	@Override
	public void paint(Graphics graphics) {
		graphics.setForegroundColor(NICE_GREEN);
		graphics.setLineWidth(4);
		graphics.drawRectangle(getClientArea());
		super.paint(graphics);
	}

}
