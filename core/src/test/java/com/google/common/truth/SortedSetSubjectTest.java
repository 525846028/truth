/*
 * Copyright (c) 2011 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static java.util.Collections.unmodifiableSortedSet;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link SortedSetSubject}. */
@RunWith(JUnit4.class)
public class SortedSetSubjectTest extends BaseSubjectTestCase {
  private static final SortedSet<String> NULL_SET;

  static {
    TreeSet<String> nullSet = Sets.newTreeSet(Ordering.natural().nullsFirst());
    nullSet.add(null);
    NULL_SET = unmodifiableSortedSet(nullSet);
  }

  /*
   * Even if we wrap a Set/SortedSet in a NavigableSet wrapper, the main operations (isInstanceOf,
   * etc.) should operate on the original Set.
   */
  @SuppressWarnings("TruthSelfEquals")
  @Test
  public void verifyWrappingIsInternalOnly() {
    Set<?> map = new HashSet<>();
    assertThat(map).isSameInstanceAs(map);
    SortedSet<?> sortedSet = unmodifiableSortedSet(new TreeSet<>());
    assertThat(sortedSet).isSameInstanceAs(sortedSet);
  }

  @Test
  public void verifyNamed() {
    @SuppressWarnings("unused")
    SortedSetSubject unused = assertWithMessage("foo").that(ImmutableSortedSet.of());
  }

  @Test
  public void hasFirstLastElement() {
    assertThat(ImmutableSortedSet.of(1, 2)).hasFirstElement(1);
    assertThat(ImmutableSortedSet.of(1, 2)).hasLastElement(2);
    assertThat(NULL_SET).hasFirstElement(null);
    assertThat(NULL_SET).hasLastElement(null);
  }

  @Test
  public void hasFirstElement_empty() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of()).hasFirstElement(1);
    assertFailureKeys("expected to have first element", "but was");
    assertFailureValue("expected to have first element", "1");
    assertFailureValue("but was", "[]");
  }

  @Test
  public void hasLastElement_empty() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of()).hasLastElement(1);
    assertFailureKeys("expected to have last element", "but was");
    assertFailureValue("expected to have last element", "1");
    assertFailureValue("but was", "[]");
  }

  @Test
  public void hasFirstElement_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of()).hasFirstElement(null);
    assertFailureKeys("expected to have first element", "but was");
    assertFailureValue("expected to have first element", "null");
    assertFailureValue("but was", "[]");
  }

  @Test
  public void hasLastElement_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of()).hasLastElement(null);
    assertFailureKeys("expected to have last element", "but was");
    assertFailureValue("expected to have last element", "null");
    assertFailureValue("but was", "[]");
  }

  @Test
  public void hasFirstLastElement_wrongPosition() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of(0, 1, 2)).hasFirstElement(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <[0, 1, 2]> has first element <1>. "
                + "It does contain this element, but the first element is <0>");
  }

  @Test
  public void hasFirstLastElement_wrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of(0, 1, 2)).hasLastElement(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <[0, 1, 2]> has last element <1>. "
                + "It does contain this element, but the last element is <2>");
  }

  @Test
  public void hasFirstLastElement_absent() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of(0)).hasFirstElement(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <[0]> has first element <1>. "
                + "It does not contain this element, and the first element is <0>");
  }

  @Test
  public void hasFirstLastElement_absent_2() {
    expectFailureWhenTestingThat(ImmutableSortedSet.of(0)).hasLastElement(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <[0]> has last element <1>. "
                + "It does not contain this element, and the last element is <0>");
  }

  private SortedSetSubject expectFailureWhenTestingThat(SortedSet<?> actual) {
    return expectFailure.whenTesting().that(actual);
  }
}
