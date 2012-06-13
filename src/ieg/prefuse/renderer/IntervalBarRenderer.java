package ieg.prefuse.renderer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import prefuse.render.AbstractShapeRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * <p>
 * Renders a {@link VisualItem} with an interval.
 * </p>
 * The interval is determined by the {@link VisualItem}s X Field (see
 * {@link VisualItem#X} and {@link VisualItem#getX()}) and the
 * {@link IntervalBarRenderer}s maxX (see
 * {@link IntervalBarRenderer#IntervalBarRenderer(String, String)} and
 * {@link IntervalBarRenderer#getMaxXField()}) field. The rendered height is
 * determined by the height of the {@link VisualItem}s font.
 * 
 * @author peterw
 * @see IntervalLayout
 */
public class IntervalBarRenderer extends AbstractShapeRenderer {
	protected Rectangle2D bounds = new Rectangle2D.Double();
	private String textField;
	protected String maxXField;

	/**
	 * Create a {@link IntervalBarRenderer}. Uses the given text data field
	 * to draw it's text label and the maxX data field to determine the interval
	 * to be rendered, which is from {@link VisualItem}s x data field to the
	 * given maxX data field.
	 * 
	 * @param textField
	 *            the data field used for the text label
	 * @param maxXField
	 *            the data field used for the interval
	 */
	public IntervalBarRenderer(String textField, String maxXField) {
		this.textField = textField;
		this.maxXField = maxXField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.render.AbstractShapeRenderer#render(java.awt.Graphics2D,
	 * prefuse.visual.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
		renderBackground(g, item);
		renderText(g, item, (int) item.getX());
	}

	/**
	 * Renders the background. Override this method to customize background
	 * painting.
	 * 
	 * @param g
	 *            graphics object
	 * @param item
	 *            the item to be rendered
	 */
	protected void renderBackground(Graphics2D g, VisualItem item) {
		super.render(g, item);
	}

	/**
	 * Renders the text of the given item at the items y and the given x
	 * position. The text may exceed the items bounds.
	 * 
	 * @param g
	 *            graphics object
	 * @param item
	 *            the item to be rendered
	 * @param x
	 *            the x position of the text
	 */
	protected void renderText(Graphics2D g, VisualItem item, int x) {
		String text = getText(item);
		int textColor = item.getTextColor();
		if (text != null && ColorLib.alpha(textColor) > 0) {
			g.setPaint(ColorLib.getColor(textColor));
			g.setFont(item.getFont());

			int y = (int) (item.getBounds().getY() + DEFAULT_GRAPHICS
					.getFontMetrics(item.getFont()).getAscent());
			g.drawString(text, x, y);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem
	 * )
	 */
	protected Shape getRawShape(VisualItem item) {
		double startX = item.getX();
		double endX = item.getDouble(maxXField);
		double y = item.getY();
		double height = Renderer.DEFAULT_GRAPHICS
				.getFontMetrics(item.getFont()).getHeight();
		double width = endX - startX;

		bounds.setFrame(startX, y, width, height);

		return bounds;
	}

	/**
	 * Returns the text of the given item, based on the text data field of this
	 * {@link IntervalBarRenderer}.
	 * 
	 * @param item
	 *            a {@link VisualItem}
	 * @return the text of the item
	 */
	protected String getText(VisualItem item) {
		String s = null;
		if (item.canGetString(textField)) {
			return item.getString(textField);
		}
		return s;
	}

	public String getMaxXField() {
		return maxXField;
	}

	public void setMaxXField(String maxXField) {
		this.maxXField = maxXField;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}
}
