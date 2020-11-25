package com.bsbls.table;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;

public class WaitLayerUI<V extends JComponent> extends LayerUI<V> implements ActionListener {
  private boolean mIsRunning;
  private boolean mIsFadingOut;
  private Timer mTimer;

  private int mAngle;
  private int mFadeCount;
  private int mFadeLimit = 15;

  @Override
  public void paint(Graphics g, JComponent c) {
    int w = c.getWidth();
    int h = c.getHeight();

    // Paint the view.
    super.paint(g, c);

    if (!mIsRunning) {
      return;
    }

    Graphics2D g2 = (Graphics2D) g.create();

    float fade = (float) mFadeCount / (float) mFadeLimit;
    // Gray it out.
    Composite urComposite = g2.getComposite();
    g2.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, .3f * fade));
    g2.fillRect(0, 0, w, h);
    g2.setComposite(urComposite);

    // Paint the wait indicator.
    int s = Math.min(w, h) / 5;
    int cx = w / 2;
    int cy = h / 2;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setStroke(
            new BasicStroke(s / 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2.setPaint(Color.white);
    g2.rotate(Math.PI * mAngle / 180, cx, cy);
    for (int i = 0; i < 12; i++) {
      float scale = (11.0f - (float) i) / 11.0f;
      g2.drawLine(cx + s, cy, cx + s * 2, cy);
      g2.rotate(-Math.PI / 6, cx, cy);
      g2.setComposite(AlphaComposite.getInstance(
              AlphaComposite.SRC_OVER, scale * fade));
    }

    g2.dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (mIsRunning) {
      firePropertyChange("tick", 0, 1);
      mAngle += 3;
      if (mAngle >= 360) {
        mAngle = 0;
      }
      if (mIsFadingOut) {
        if (--mFadeCount == 0) {
          mIsRunning = false;
          mTimer.stop();
        }
      } else if (mFadeCount < mFadeLimit) {
        mFadeCount++;
      }
    }
  }

  public void start() {
    if (mIsRunning) {
      return;
    }

    // Run a thread for animation.
    mIsRunning = true;
    mIsFadingOut = false;
    mFadeCount = 0;
    int fps = 24;
    int tick = 1000 / fps;
    mTimer = new Timer(tick, this);
    mTimer.start();
  }

  public void stop() {
    mIsFadingOut = true;
  }


  public void installUI(JComponent c) {
    super.installUI(c);
    // enable mouse motion events for the layer's subcomponents
    ((JLayer) c).setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
  }

  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    // reset the layer event mask
    ((JLayer) c).setLayerEventMask(0);
  }


  @Override
  public void eventDispatched(AWTEvent e, JLayer<? extends V> l) {
    if(mIsRunning) {
      if(e instanceof InputEvent) {
        ((InputEvent) e).consume();
      }
    }
    else {
      super.eventDispatched(e, l);
    }
  }

  @Override
  public void applyPropertyChange(PropertyChangeEvent pce, JLayer l) {
    if ("tick".equals(pce.getPropertyName())) {
      l.repaint();
    }
  }
}