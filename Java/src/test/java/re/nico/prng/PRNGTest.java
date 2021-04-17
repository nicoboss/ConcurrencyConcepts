package re.nico.prng;

import org.junit.jupiter.api.Test;

final class PRNGTest {

    @Test
    void testReactivePRNG() throws InterruptedException {
        new ReactivePRNG().run();
    }

}
