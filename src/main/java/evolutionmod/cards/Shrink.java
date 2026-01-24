package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.DrawPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.AbsorptionPower;

import java.util.List;
import java.util.stream.Collectors;

public class Shrink
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Shrink";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Shrink2.png";
    private static final int COST = 0;
    private static final int GAIN_DRAW_AMT = 1;
    private static final int EVOKE_AMT = 10;
    private static final int UPGRADED_EVOKE_AMT = 2;

    public Shrink() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = GAIN_DRAW_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        List<AbstractOrb> orbs = p.orbs.stream()
                .filter(o -> o instanceof AbstractGene)
                .limit(this.upgraded ? UPGRADED_EVOKE_AMT : EVOKE_AMT)
                .collect(Collectors.toList());
        orbs.forEach(o -> {
            this.addToBot(new EvokeSpecificOrbAction(o));
        });
        addToBot(new ApplyPowerAction(p, p, new DrawPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}