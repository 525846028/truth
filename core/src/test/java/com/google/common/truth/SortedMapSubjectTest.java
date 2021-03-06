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
import static java.util.Collections.unmodifiableSortedMap;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link SortedMapSubject}. */
@RunWith(JUnit4.class)
public class SortedMapSubjectTest extends BaseSubjectTestCase {
  private static final SortedMap<String, Object> NULL_MAP;

  static {
    TreeMap<String, Object> nullMap = Maps.newTreeMap(Ordering.natural().nullsFirst());
    nullMap.put(null, null);
    NULL_MAP = unmodifiableSortedMap(nullMap);
  }

  /*
   * Even if we wrap a Map/SortedMap in a NavigableMap wrapper, the main operations (isInstanceOf,
   * etc.) should operate on the original Map.
   */
  @SuppressWarnings("TruthSelfEquals")
  @Test
  public void verifyWrappingIsInternalOnly() {
    Map<?, ?> map = new HashMap<>();
    assertThat(map).isSameInstanceAs(map);
    SortedMap<?, ?> sortedMap = unmodifiableSortedMap(new TreeMap<>());
    assertThat(sortedMap).isSameInstanceAs(sortedMap);
  }

  @Test
  public void verifyNamed() {
    @SuppressWarnings("unused")
    SortedMapSubject unused = assertWithMessage("foo").that(ImmutableSortedMap.of());
  }

  @Test
  public void hasFirstLastKey() {
    assertThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasFirstKey(1);
    assertThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasLastKey(2);
    assertThat(NULL_MAP).hasFirstKey(null);
    assertThat(NULL_MAP).hasLastKey(null);
  }

  @Test
  public void hasFirstKey_empty() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasFirstKey(1);
    assertFailureKeys("expected to have first key", "but was");
    assertFailureValue("expected to have first key", "1");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasLastKey_empty() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasLastKey(1);
    assertFailureKeys("expected to have last key", "but was");
    assertFailureValue("expected to have last key", "1");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasFirstKey_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasFirstKey(null);
    assertFailureKeys("expected to have first key", "but was");
    assertFailureValue("expected to have first key", "null");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasLastKey_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasLastKey(null);
    assertFailureKeys("expected to have last key", "but was");
    assertFailureValue("expected to have last key", "null");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasFirstLastKey_wrongPosition_1() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasFirstKey(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has first key <1>. "
                + "It does contain this key, but the first key is <0>");
  }

  @Test
  public void hasFirstLastKey_wrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasLastKey(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has last key <1>. "
                + "It does contain this key, but the last key is <2>");
  }

  @Test
  public void hasFirstLastKey_absent_1() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0)).hasFirstKey(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0}> has first key <1>. "
                + "It does not contain this key, and the first key is <0>");
  }

  @Test
  public void hasFirstLastKey_absent_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0)).hasLastKey(1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0}> has last key <1>. "
                + "It does not contain this key, and the last key is <0>");
  }

  @Test
  public void hasFirstLastEntry() {
    assertThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasFirstEntry(1, 0);
    assertThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasLastEntry(2, 0);
    assertThat(NULL_MAP).hasFirstEntry(null, null);
    assertThat(NULL_MAP).hasLastEntry(null, null);
  }

  @Test
  public void hasFirstLastEntry_onSortedMap() {
    assertThat(unmodifiableSortedMap(ImmutableSortedMap.of(1, 0, 2, 0))).hasFirstEntry(1, 0);
    assertThat(unmodifiableSortedMap(ImmutableSortedMap.of(1, 0, 2, 0))).hasLastEntry(2, 0);
  }

  @Test
  public void hasFirstEntry_empty() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasFirstEntry(1, 0);
    assertFailureKeys("expected to have first entry", "but was");
    assertFailureValue("expected to have first entry", "1=0");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasLastEntry_empty() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasLastEntry(1, 0);
    assertFailureKeys("expected to have last entry", "but was");
    assertFailureValue("expected to have last entry", "1=0");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasFirstEntry_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasFirstEntry(null, null);
    assertFailureKeys("expected to have first entry", "but was");
    assertFailureValue("expected to have first entry", "null=null");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasLastEntry_empty_null() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of()).hasLastEntry(null, null);
    assertFailureKeys("expected to have last entry", "but was");
    assertFailureValue("expected to have last entry", "null=null");
    assertFailureValue("but was", "{}");
  }

  @Test
  public void hasFirstLastEntry_wrongPosition() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasFirstEntry(1, 0);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has first entry <1=0>. "
                + "It does contain this entry, but the first entry is <0=0>");
  }

  @Test
  public void hasFirstLastEntry_wrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasLastEntry(1, 0);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has last entry <1=0>. "
                + "It does contain this entry, but the last entry is <2=0>");
  }

  @Test
  public void hasFirstLastEntry_wrongKey() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasFirstEntry(0, 0);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo("Not true that <{1=0, 2=0}> has first entry <0=0>, the first key is <1>");
  }

  @Test
  public void hasFirstLastEntry_wrongKey_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasLastEntry(0, 0);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo("Not true that <{1=0, 2=0}> has last entry <0=0>, the last key is <2>");
  }

  @Test
  public void hasFirstLastEntry_wrongValue() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasFirstEntry(1, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo("Not true that <{1=0, 2=0}> has first entry <1=1>, the first value is <0>");
  }

  @Test
  public void hasFirstLastEntry_wrongValue_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0, 2, 0)).hasLastEntry(2, 2);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo("Not true that <{1=0, 2=0}> has last entry <2=2>, the last value is <0>");
  }

  @Test
  public void hasFirstLastEntry_keyWrongPosition() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasFirstEntry(1, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has first entry <1=1>. It does contain this key, "
                + "but the key is mapped to <0>, and the first entry is <0=0>");
  }

  @Test
  public void hasFirstLastEntry_keyWrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 0, 2, 0)).hasLastEntry(1, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=0, 2=0}> has last entry <1=1>. It does contain this key, "
                + "but the key is mapped to <0>, and the last entry is <2=0>");
  }

  @Test
  public void hasFirstLastEntry_valueWrongPosition() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 1, 2, 2)).hasFirstEntry(10, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=1, 2=2}> has first entry <10=1>. It does contain this value, "
                + "but the value is mapped from the keys <[1]>, and the first entry is <0=0>");
  }

  @Test
  public void hasFirstLastEntry_valueWrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 1, 2, 2)).hasLastEntry(10, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=1, 2=2}> has last entry <10=1>. It does contain this value, "
                + "but the value is mapped from the keys <[1]>, and the last entry is <2=2>");
  }

  @Test
  public void hasFirstLastEntry_multipleValuesWrongPosition() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 1, 2, 1, 3, 3))
        .hasFirstEntry(10, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=1, 2=1, 3=3}> has first entry <10=1>. "
                + "It does contain this value, but the value is mapped from the keys <[1, 2]>, "
                + "and the first entry is <0=0>");
  }

  @Test
  public void hasFirstLastEntry_multipleValuesWrongPosition_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(0, 0, 1, 1, 2, 1, 3, 3)).hasLastEntry(10, 1);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{0=0, 1=1, 2=1, 3=3}> has last entry <10=1>. "
                + "It does contain this value, but the value is mapped from the keys <[1, 2]>, "
                + "and the last entry is <3=3>");
  }

  @Test
  public void hasFirstLastEntry_absent() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0)).hasFirstEntry(2, 2);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{1=0}> has first entry <2=2>. "
                + "It does not contain this entry, and the first entry is <1=0>");
  }

  @Test
  public void hasFirstLastEntry_absent_2() {
    expectFailureWhenTestingThat(ImmutableSortedMap.of(1, 0)).hasLastEntry(2, 2);
    assertThat(expectFailure.getFailure())
        .hasMessageThat()
        .isEqualTo(
            "Not true that <{1=0}> has last entry <2=2>. "
                + "It does not contain this entry, and the last entry is <1=0>");
  }

  private SortedMapSubject expectFailureWhenTestingThat(SortedMap<?, ?> actual) {
    return expectFailure.whenTesting().that(actual);
  }
}
