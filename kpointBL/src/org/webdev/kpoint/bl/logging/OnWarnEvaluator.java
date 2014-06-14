package org.webdev.kpoint.bl.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
  
  /**
   * Evaluates to true when the logging event passed as parameter has level WARN or higher.
   * 
   * @author Dawson Mossman
   * 
   */
  public class OnWarnEvaluator extends EventEvaluatorBase<ILoggingEvent> {
    
	  /**
	 * Return true if event passed as parameter has level ERROR or higher, returns
	 * false otherwise.
	 */
     public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
    	 return event.getLevel().levelInt == Level.WARN_INT;
     }
  }
