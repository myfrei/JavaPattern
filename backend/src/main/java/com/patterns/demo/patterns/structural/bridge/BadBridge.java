package com.patterns.demo.patterns.structural.bridge;

/**
 * Антипаттерн вместо Bridge — комбинаторный взрыв подклассов.
 *
 * Без моста на каждую пару «фигура × рендерер» заводят отдельный класс. Два
 * измерения по 2 значения = 4 класса; добавили рендерер — ещё по классу на
 * каждую фигуру. Растёт как произведение, а не как сумма.
 */
public final class BadBridge {

    public static final class VectorCircle { public String draw() { return "vector[circle]"; } }
    public static final class RasterCircle { public String draw() { return "raster(circle)"; } }
    public static final class VectorSquare { public String draw() { return "vector[square]"; } }
    public static final class RasterSquare { public String draw() { return "raster(square)"; } }

    /** 2 фигуры × 2 рендерера = 4 класса (3×3 было бы уже 9). */
    public static int classCount() { return 4; }

    private BadBridge() {}
}
