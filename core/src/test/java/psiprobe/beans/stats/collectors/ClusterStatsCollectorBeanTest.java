/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.ClusterStatsCollectorBean;

/**
 * The Class ClusterStatsCollectorBeanTest.
 */
public class ClusterStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterStatsCollectorBean.class).loadData().test();
  }

}
