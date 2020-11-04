package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;
import java.util.stream.Collectors;

public class FireAnts
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:FireAnts";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkSkl.png";
    private static final int COST = 1;
    private static final int EVOKE_AMT = 1;
    private static final int UPGRADE_EVOKE_AMT = 2;

    public FireAnts() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = EVOKE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int maxEvoke = this.magicNumber;
        if (this.upgraded || !AbstractGene.isPlayerInThisForm(InsectGene.ID)) {
            addToBot(new ChannelAction(new InsectGene()));
        } else {
            maxEvoke += 1;
        }
        if (this.upgraded || AbstractGene.isPlayerInThisForm(InsectGene.ID)) {
            addToBot(new ChannelAction(new LavafolkGene()));
        } else {
            maxEvoke += 1;
        }
        p.orbs.stream()
                .filter(o -> LavafolkGene.ID.equals(o.ID))
                .limit(maxEvoke)
                .collect(Collectors.toList()) // dissociate from initial list before evoking
//        toEvoke
                .forEach(o -> {
                    addToBot(new EvokeSpecificOrbAction(o));
                    addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p)));
                });
//                .collect(Collectors.toList());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_EVOKE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}