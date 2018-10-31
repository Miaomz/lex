package org.casual.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author miaomuzhi
 * @since 2018/10/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<K, V> {

    private K key;

    private V val;
}
