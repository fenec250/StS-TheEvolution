package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnChannelRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.AbstractGene;

public class OldOutfit extends CustomRelic implements OnChannelRelic {
    public static final String ID = "evolutionmod:OldOutfit";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/OldOutfit.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/OldOutfit_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);

    public OldOutfit() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.beginLongPulse();
    }

    @Override
    public void atTurnStart() {
        this.beginLongPulse();
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        if (this.pulse && orb instanceof AbstractGene) {
            this.stopPulse();
            AbstractPlayer player = AbstractDungeon.player;

            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    boolean result = player.orbs.remove(orb);
                    if (result) {
                        player.orbs.add(new EmptyOrbSlot(player.drawX, player.drawY));
                        for (int i = 0; i < player.orbs.size(); ++i) {
                            player.orbs.get(i).setSlot(i, player.maxOrbs);
                        }
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.stopPulse();
    }
}