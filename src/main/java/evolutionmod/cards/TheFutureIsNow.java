package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import evolutionmod.actions.FateAction;
import evolutionmod.actions.FutureNowAction;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;

public class TheFutureIsNow extends BaseEvoCard {
    public static final String ID = "evolutionmod:TheFutureIsNow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = -1;
    private static final int FORM_FATE_AMT = 2;

    public TheFutureIsNow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORM_FATE_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

		formEffect(LymeanGene.ID, () -> addToBot(new FateAction(FORM_FATE_AMT)));
		addToBot(new FutureNowAction(x));

		addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!freeToPlayOnce) {
                    p.energy.use(energyOnUse);
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene.ID)) {
            this.glowColor = LymeanGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}