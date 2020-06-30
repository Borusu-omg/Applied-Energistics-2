/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.client.render.effects;

import java.util.Random;

import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class LightningArcFX extends LightningFX {

    private static final Random RANDOM_GENERATOR = new Random();

    private final double rx;
    private final double ry;
    private final double rz;

    public LightningArcFX(ClientWorld world, final double x, final double y, final double z, final double ex,
            final double ey, final double ez, final double r, final double g, final double b) {
        super(world, x, y, z, r, g, b, 6);

        this.rx = ex - x;
        this.ry = ey - y;
        this.rz = ez - z;

        this.regen();
    }

    @Override
    protected void regen() {
        final double i = 1.0 / (this.getSteps() - 1);
        final double lastDirectionX = this.rx * i;
        final double lastDirectionY = this.ry * i;
        final double lastDirectionZ = this.rz * i;

        final double len = Math.sqrt(
                lastDirectionX * lastDirectionX + lastDirectionY * lastDirectionY + lastDirectionZ * lastDirectionZ);
        for (int s = 0; s < this.getSteps(); s++) {
            final double[][] localSteps = this.getPrecomputedSteps();

            localSteps[s][0] = (lastDirectionX + (RANDOM_GENERATOR.nextDouble() - 0.5) * len * 1.2) / 2.0;
            localSteps[s][1] = (lastDirectionY + (RANDOM_GENERATOR.nextDouble() - 0.5) * len * 1.2) / 2.0;
            localSteps[s][2] = (lastDirectionZ + (RANDOM_GENERATOR.nextDouble() - 0.5) * len * 1.2) / 2.0;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<LightningArcParticleData> {
        private final SpriteProvider spriteSet;

        public Factory(SpriteProvider spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(LightningArcParticleData effect, ClientWorld world, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            SpriteBillboardParticle lightningFX = new LightningArcFX(world, x, y, z, effect.target.x, effect.target.y,
                    effect.target.z, 0, 0, 0);
            lightningFX.setSprite(this.spriteSet);
            return lightningFX;
        }
    }

}