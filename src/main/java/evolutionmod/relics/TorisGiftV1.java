package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbsV1.*;

import java.util.ArrayList;

public class TorisGiftV1 extends CustomRelic {
    public static final String ID = "evolutionmod:TorisGiftV1";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(TorisGift.ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/TorisGift.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/TorisGift_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int GENE_AMT = 1;

    public TorisGiftV1() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
//    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
//        this.addToBot(new IncreaseMaxOrbAction(SLOT_AMT));

        ArrayList<String> genesPool = new ArrayList<>();
        genesPool.add(CentaurGene.ID);
        genesPool.add(PlantGene.ID);
        genesPool.add(MerfolkGene.ID);
        genesPool.add(HarpyGene.ID);
        genesPool.add(LavafolkGene.ID);
        genesPool.add(SuccubusGene.ID);
        genesPool.add(LymeanGene.ID);
        genesPool.add(InsectGene.ID);
        genesPool.add(BeastGene.ID);
        genesPool.add(LizardGene.ID);
        genesPool.add(ShadowGene.ID);

        for (int i = 0; i < GENE_AMT; ++i) {
            String geneId = genesPool.get(AbstractDungeon.cardRandomRng.random(genesPool.size() - 1));
            AbstractDungeon.actionManager.addToBottom(BaseEvoCard.getGene(geneId).getChannelAction());
//            addToBot(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    gene.onStartOfTurn();
//                    this.isDone = true;
//                }
//            });
        }
        this.grayscale = true;
    }

    public void onVictory() {
        this.grayscale = false;
    }
}