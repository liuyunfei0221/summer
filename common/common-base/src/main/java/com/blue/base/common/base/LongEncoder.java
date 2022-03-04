package com.blue.base.common.base;

/**
 * encode long util
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class LongEncoder {

    private static final long SEED = 19890221L;

    private final long SALT_A;

    private final long SALT_B;

    public LongEncoder(String salt) {
        long offset = Long.MAX_VALUE & salt.hashCode() | SEED;
        System.err.println(offset);

        SALT_A = (offset & 1L) == 0L ? offset << 32 : ~offset << 32;
        System.err.println(SALT_A);

        SALT_B = (SALT_A & 1L) == 0L ? SALT_A | offset : ~SALT_A | offset;
        System.err.println(SALT_B);
    }

    private Long encrypt(Long originalData) {
        if (originalData != null) {
            long tar = originalData ^ SALT_A;
            tar = tar ^ SALT_B;

            return tar;
        }
        return null;
    }

    private Long decrypt(Long encryptData) {
        if (encryptData != null) {
            long tar = encryptData ^ SALT_B;
            tar = tar ^ SALT_A;

            return tar;
        }

        return null;
    }

    /**
     * encrypt Long
     *
     * @param originalData
     * @return
     */
    public Long encryptLong(Long originalData) {
        return this.encrypt(originalData);
    }

    /**
     * decrypt Long
     *
     * @param encryptData
     * @return
     */
    public Long decryptLong(Long encryptData) {
        return this.decrypt(encryptData);
    }

}
